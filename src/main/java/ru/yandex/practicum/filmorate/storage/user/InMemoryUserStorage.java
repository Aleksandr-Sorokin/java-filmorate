package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();
    private static Integer globalIdUser = 1;

    public Map<Integer, User> getUsers() {
        return users;
    }

    private static Integer createNextId(){
        return globalIdUser++;
    }

    @Override
    public User addUser(User user) {
        if (UserStorage.checkValidationUser(user, log)){
            user.setId(createNextId());
            users.put(user.getId(), user);
            log.info("Успешное добавление пользователя");
            return user;
        }
        return user;
    }

    @Override
    public void deleteUser(Integer idUser) {
        users.remove(idUser);
    }

    @Override
    public User changeUser(User user) {
        if (!users.containsKey(user.getId())){
            throw new FilmUserNotFoundException(String.format("Пользователя с id %s нет", user.getId()));
        }
        if (UserStorage.checkValidationUser(user, log)){
            users.put(user.getId(), user);
            log.info("Успешное изменение пользователя");
        }
        return user;
    }

    public List<User> getAllUsers(){
        return users.values().stream().collect(Collectors.toList());
    }

    public User findUserById(Integer id){
        if (users.get(id) == null){
            throw new FilmUserNotFoundException("Нет такого пользователя");
        }
        return users.get(id);
    }
}
