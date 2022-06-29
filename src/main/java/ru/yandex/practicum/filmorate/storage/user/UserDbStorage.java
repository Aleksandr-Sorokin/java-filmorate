package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Set<Integer> uploadFriends(Integer userId, Boolean status){
        String sql = "SELECT friend_id FROM users_friends " +
                "WHERE user_id = ? AND status_friendship = ?";
        return new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getInt("friend_id"),
                userId, status));
    }

    private User uploadUser(ResultSet result) throws SQLException {
        Integer id = result.getInt("user_id");
        String email = result.getString("user_email");
        String name = result.getString("user_name");
        String login = result.getString("user_login");
        LocalDate birthday = result.getDate("user_birthday").toLocalDate();
        User user = new User(email, name, login, birthday);
        user.setId(id);
        Set<Integer> friend = uploadFriends(id, true);
        friend.addAll(uploadFriends(id, false));
        user.setFriends(friend);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT user_id, user_name, user_birthday, user_login, user_email FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> uploadUser(rs));
    }

    @Override
    public User addUser(User user) {
        if (UserStorage.checkValidationUser(user, log)){
            Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("USERS")
                    .usingColumns("user_name", "user_birthday", "user_login", "user_email")
                    .usingGeneratedKeyColumns("user_id")
                    .executeAndReturnKeyHolder(Map.of("user_name", user.getName(),
                            "user_birthday", user.getBirthday(),
                            "user_login", user.getLogin(),
                            "user_email", user.getEmail()))
                    .getKeys();
            user.setId((Integer) keys.get("user_id"));
            insertFriends(user);
            return user;
        }  else {
            throw new ValidationException("Пользователь не добавлен");
        }
    }

    private void insertFriends(User user){
        if (!user.getFriends().isEmpty()){
            Set<Integer> friends = user.getFriends();
            for (Integer friend: friends){
                String sql = "INSERT INTO users_friends (user_id, friend_id, status_friendship) VALUES (?, ?, ?)";
                if (findUserById(friend).getFriends().contains(user.getId())){
                    jdbcTemplate.update(sql,user.getId(), friend, true);
                } else {
                    jdbcTemplate.update(sql,user.getId(), friend, false);
                }
            }
        }
    }

    @Override
    public void deleteUser(Integer idUser) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql,idUser);
    }

    @Override
    public User changeUser(User user) {
        UserStorage.checkValidationUser(user, log);
        findUserById(user.getId());
        String updateUsers = "UPDATE users SET user_email = ?, user_name = ?, user_login = ?, user_birthday = ?" +
                " WHERE user_id = ?";
        String removeFriend = "DELETE FROM USERS_FRIENDS WHERE user_id = ? AND friend_id = ?";
        //удаляет из базы лишние записи с друзьями
        Set<Integer> onRemove = uploadFriends(user.getId(), true);
        onRemove.addAll(uploadFriends(user.getId(), false));
        Set<Integer> friends = user.getFriends();
        onRemove.removeAll(friends);
        for (Integer deleteFriend : onRemove){
            jdbcTemplate.update(removeFriend,user.getId(),deleteFriend);
        }
        insertFriends(user);
        jdbcTemplate.update(updateUsers,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", id);
        if (sqlRow.next()){
            log.info("Пользователь найден: {} {}",
                    sqlRow.getInt("user_id"), sqlRow.getString("user_login"));
            User user = new User(
                    sqlRow.getString("user_email"),
                    sqlRow.getString("user_name"),
                    sqlRow.getString("user_login"),
                    sqlRow.getDate("user_birthday").toLocalDate());
            user.setId(sqlRow.getInt("user_id"));
            Set<Integer> friends = uploadFriends(id, true);
            Set<Integer> follows = uploadFriends(id, false);
            friends.addAll(follows);
            user.setFriends(friends);
            return user;
        } else {
            throw new FilmUserNotFoundException("Нет такого пользователя");
        }
    }
}
