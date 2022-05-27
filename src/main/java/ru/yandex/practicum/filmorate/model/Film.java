package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NotNull @NotBlank
    private final String name;
    @NotNull @NotBlank @Size(max = 200)
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Duration duration;
    private Set<Integer> like;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getLike() {
        return like;
    }
    public void setLike(int idUser) {
        this.like.add(idUser);
    }
}
