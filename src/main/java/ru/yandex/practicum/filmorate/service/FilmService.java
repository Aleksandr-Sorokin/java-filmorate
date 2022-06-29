package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage")FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer idUser, Integer idFilm){
        Film film = filmStorage.findFilmById(idFilm);
        User user = userStorage.findUserById(idUser);
        film.addLikes(idUser);
        filmStorage.changeFilm(film);
    }

    public void deleteLike(int idFilm, int idUser){
        Film film = filmStorage.findFilmById(idFilm);
        film.getLike().remove(userStorage.findUserById(idUser).getId());
        filmStorage.changeFilm(film);
    }

    public List<Film> bestFilmByLike(Integer count){
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getLike().size()).compareTo(Integer.valueOf(o2.getLike().size()));
                    return result * -1;
                }).limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(int id){
        return filmStorage.findFilmById(id);
    }
}
