package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @NotBlank @Email
    private final String email;
    @NotBlank @Pattern(regexp = "\\S+")
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
    private Set<Integer> friends;

    public User(String email, String name, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (name.isBlank()){
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}
