package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final static LocalDate MIN_DATE_START_RELEASE = LocalDate.parse("1895-12-28");

    private boolean checkValidationFilm(Film film) throws ValidationException{
        if (film.getReleaseDate().isBefore(MIN_DATE_START_RELEASE)){
            log.info("Ошибка валидации: дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза раньше 28 декабря 1895 года");
        } else if (film.getDuration().isNegative()){
            log.info("Ошибка валидации: продолжительность фильма отрицательная");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }

    @PostMapping
    public void addFilm(@Valid @RequestBody Film film) throws ValidationException{
        if (checkValidationFilm(film)){
            films.put(film.getId(), film);
            log.info("Успешное добавление фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
    }

    @PutMapping
    public void changeFilm(@Valid @RequestBody Film film) throws ValidationException{
        if (checkValidationFilm(film)){
            films.put(film.getId(), film);
            log.info("Успешное изменение фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
    }

    @GetMapping
    public Map<Integer,Film> allFilms(){
        return films;
    }
}
