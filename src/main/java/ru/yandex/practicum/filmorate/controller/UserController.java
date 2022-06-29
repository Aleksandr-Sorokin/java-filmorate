package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userDbStorage") UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Integer id){
        return userStorage.findUserById(id);
    }

    @GetMapping("/users")
    public List<User> allUsers(){
        return userStorage.getAllUsers();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int idUser, @PathVariable("otherId") int idOther){
        return userService.findCommonFriends(idUser, idOther);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Integer id){
        return userService.findAllFriends(id);
    }

    @PostMapping("/users")
    public @Valid User addUser(@Valid @RequestBody User user){
        return userStorage.addUser(user);
    }

    @ResponseBody
    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user){
        return userStorage.changeUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") int idUser, @PathVariable("friendId") int idFriends){
        userService.addFriends(idUser, idFriends);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        userStorage.deleteUser(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") int idUser, @PathVariable("friendId") int idFriends){
        userService.deleteFriends(idUser, idFriends);
    }
}
