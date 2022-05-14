package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private boolean checkValidationUser(User user) throws ValidationException{
        if (user.getBirthday().isAfter(LocalDate.now())){
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }

    @PostMapping
    public @Valid User addUser(@Valid @RequestBody User user) throws ValidationException{
        if (checkValidationUser(user)){
            users.put(user.getId(), user);
            log.info("Успешное добавление пользователя");
            return user;
        }
        return user;
    }

    @PutMapping
    public void changeUser(@Valid @RequestBody User user) throws ValidationException{
        if (checkValidationUser(user)){
            users.put(user.getId(), user);
            log.info("Успешное изменение пользователя");
        }
    }

    @GetMapping
    public Map<Integer,User> allUsers(){
        return users;
    }
}
