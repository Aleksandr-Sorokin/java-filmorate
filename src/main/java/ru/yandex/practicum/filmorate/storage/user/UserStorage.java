package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User addUser(User user);
    void deleteUser(Integer idUser);
    User changeUser(User user);
    User findUserById(Integer id);
    static boolean checkValidationUser(User user, Logger log){
        if (user.getBirthday().isAfter(LocalDate.now())){
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }
}
