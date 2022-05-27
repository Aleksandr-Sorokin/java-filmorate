package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private final static LocalDate MIN_DATE_START_RELEASE = LocalDate.parse("1895-12-28");
    private static Integer globalIdFilm = 1;

    private boolean checkValidationFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_DATE_START_RELEASE)){
            log.info("Ошибка валидации: дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза раньше 28 декабря 1895 года");
        } else if (film.getDuration().isNegative()){
            log.info("Ошибка валидации: продолжительность фильма отрицательная");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }

    private static Integer createNextId(){
        return globalIdFilm++;
    }

    @Override
    public void addFilm(Film film) {
        if (checkValidationFilm(film)){
            film.setId(createNextId());
            films.put(film.getId(), film);
            log.info("Успешное добавление фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
    }

    @Override
    public void deleteFilm(Integer idFilm) {
        films.remove(idFilm);
    }

    @Override
    public void changeFilm(Film film) {
        if (checkValidationFilm(film)){
            films.put(film.getId(), film);
            log.info("Успешное изменение фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }
}
