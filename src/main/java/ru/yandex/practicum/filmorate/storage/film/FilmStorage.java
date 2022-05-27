package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Map;

public interface FilmStorage {
    void addFilm(Film film);

    void deleteFilm(Integer idFilm);

    void changeFilm(Film film);

    Map<Integer, Film> getAllFilms();
}
