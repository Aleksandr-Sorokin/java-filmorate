package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    void deleteFilm(Integer idFilm);
    Film changeFilm(Film film);
    List<Film> getAllFilms();
    Film findFilmById(int idFilm);
}
