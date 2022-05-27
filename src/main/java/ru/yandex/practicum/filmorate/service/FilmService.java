package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addLike(int idUser, int idFilm){
        Film film = inMemoryFilmStorage.findFilmById(idFilm);
        User user = inMemoryUserStorage.findUserById(idUser);
        film.setLike(user.getId());
    }

    public void deleteLike(int idFilm, int idUser){
        Film film = inMemoryFilmStorage.findFilmById(idFilm);
        User user = inMemoryUserStorage.findUserById(idUser);
        film.getLike().remove(user.getId());
    }

    public List<Film> bestFilmByLike(Integer count){
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getLike().size()).compareTo(Integer.valueOf(o2.getLike().size()));
                    return result * -1;
                }).limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(int id){
        if (id <= 0){
            throw new FilmUserNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        Film film = inMemoryFilmStorage.getAllFilms().get(id);
        return film;
    }
}
