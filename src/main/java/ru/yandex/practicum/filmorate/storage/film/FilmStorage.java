package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmStorage {
    LocalDate MIN_DATE_START_RELEASE = LocalDate.parse("1895-12-28");

    Film addFilm(Film film);

    void deleteFilm(Integer idFilm);

    Film changeFilm(Film film);

    List<Film> getAllFilms();

    Film findFilmById(int idFilm);

    static boolean checkValidationFilm(Film film, Logger log) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_DATE_START_RELEASE)){
            log.info("Ошибка валидации: дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0){
            log.info("Ошибка валидации: продолжительность фильма отрицательная");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
