package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> getAllUsers();
    User addUser(User user);
    void deleteUser(Integer idUser);
    User changeUser(User user);
    User findUserById(Integer id);
}
