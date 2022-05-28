package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
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
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();
    private static Integer globalIdUser = 1;

    public Map<Integer, User> getUsers() {
        return users;
    }

    private static Integer createNextId(){
        return globalIdUser++;
    }

    private boolean checkValidationUser(User user){
        if (user.getBirthday().isAfter(LocalDate.now())){
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }

    @Override
    public User addUser(User user) {
        if (checkValidationUser(user)){
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
        if (user.getId() == null){
            throw new ValidationException("Отсутствует id пользователя");
        }
        if (!users.containsKey(user.getId())){
            throw new FilmUserNotFoundException(String.format("Пользователя с id %s нет", user.getId()));
        }
        if (checkValidationUser(user)){
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
