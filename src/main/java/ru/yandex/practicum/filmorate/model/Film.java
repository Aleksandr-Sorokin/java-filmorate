package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private final int id;
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @NotBlank
    @Size(max = 200)
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Duration duration;
}
