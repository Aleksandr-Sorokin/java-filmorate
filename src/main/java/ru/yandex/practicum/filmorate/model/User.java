package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {
    @Id
    private final int id;
    @NotNull
    @NotBlank
    @Email
    private final String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\S+")
    private final String login;
    private final String name;
    @NotNull
    private final LocalDate birthday;

    public User(int id, String email, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = login;
        this.birthday = birthday;
    }
}
