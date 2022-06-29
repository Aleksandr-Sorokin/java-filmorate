package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films;

    private static Integer globalIdFilm = 1;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    private static Integer createNextId(){
        return globalIdFilm++;
    }

    @Override
    public Film addFilm(Film film) {
        if (FilmStorage.checkValidationFilm(film, log)){
            film.setId(createNextId());
            films.put(film.getId(), film);
            log.info("Успешное добавление фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
        return film;
    }

    @Override
    public void deleteFilm(Integer idFilm) {
        films.remove(idFilm);
    }

    @Override
    public Film changeFilm(Film film) {
        if (film.getId() == null){
            throw new ValidationException("Отсутствует id параметр фильм");
        }
        if (film.getId() <= 0){
            throw new FilmUserNotFoundException("Такого фильма нет");
        }
        if (FilmStorage.checkValidationFilm(film, log)){
            films.put(film.getId(), film);
            log.info("Успешное изменение фильма: наименование - {}, символов в описании - {}, дата - {}, " +
                            "продолжительность - {}",film.getName(), film.getDescription().length()
                    , film.getReleaseDate(), film.getDuration());
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film findFilmById(int idFilm){
        if (idFilm <= 0){
            throw new FilmUserNotFoundException(String.format("Нет фильма с id %s", idFilm));
        }
        return films.get(idFilm);
    }
}
