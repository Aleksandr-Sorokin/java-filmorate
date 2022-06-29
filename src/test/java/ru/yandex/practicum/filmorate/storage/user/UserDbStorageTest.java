package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {

    @Qualifier("userDbStorage")
    @Autowired
    private UserStorage userStorage;

    @Test
    void getAllUsers() {
        assertFalse(userStorage.getAllUsers().isEmpty());
    }
    User user = new User(
            "email_1@yandex.ru",
            "Aleksandr",
            "login_1",
            LocalDate.of(1987, 05, 24)
    );
    User userChange = new User(
            "email_1@yandex.ru",
            "Aleks",
            "login_1",
            LocalDate.of(1987, 05, 24)
    );
    User userFuture = new User(
            "email_1@yandex.ru",
            "Aleksandr",
            "login_1",
            LocalDate.of(2987, 05, 24)
    );

    @Test
    void addUser() {

        assertThrows(ValidationException.class, () -> userStorage.addUser(userFuture));
        assertTrue(userStorage.addUser(user).getId() != null);
    }

    @Test
    void deleteUser() {
        userStorage.deleteUser(userStorage.addUser(user).getId());
        assertFalse(userStorage.getAllUsers().contains(user));
    }

    @Test
    void changeUser() {
        assertTrue(userStorage.addUser(user).getName().equals("Aleksandr"));
        assertTrue(userStorage.getAllUsers().size() == 1);
        userChange.setId(1);
        assertTrue(userStorage.changeUser(userChange).getName().equals("Aleks"));
        assertTrue(userStorage.getAllUsers().size() == 1);
    }

    @Test
    void findUserById() {
        User user1 = userStorage.addUser(user);
        assertTrue(user1.equals(userStorage.findUserById(user1.getId())));
    }
}