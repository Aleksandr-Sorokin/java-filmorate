package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final UserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Integer id){
        return inMemoryUserStorage.findUserById(id);
    }

    @GetMapping("/users")
    public List<User> allUsers(){
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int idUser, @PathVariable("otherId") int idOther){
        return userService.findCommonFriends(idUser, idOther);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Integer idUser){
        return userService.findAllFriends(idUser);
    }

    @PostMapping("/users")
    public @Valid User addUser(@Valid @RequestBody User user){
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user){
        return inMemoryUserStorage.changeUser(user);

    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") int idUser, @PathVariable("friendId") int idFriends){
        userService.addFriends(idUser, idFriends);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        inMemoryUserStorage.deleteUser(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") int idUser, @PathVariable("friendId") int idFriends){
        userService.deleteFriends(idUser, idFriends);
    }
}
