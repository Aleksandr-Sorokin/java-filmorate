package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public void addFilm(@Valid @RequestBody Film film) throws ValidationException{
        inMemoryFilmStorage.addFilm(film);
    }
    @PutMapping("/films")
    public void changeFilm(@Valid @RequestBody Film film) throws ValidationException{
        inMemoryFilmStorage.changeFilm(film);
    }
    @GetMapping("/films")
    public Map<Integer,Film> allFilms(){
        return inMemoryFilmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable int id){
        return filmService.findFilmById(id);
    }
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int userId, @PathVariable int id){
        filmService.addLike(userId, id);
    }
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> bestFilmByLike(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count){
        return filmService.bestFilmByLike(count);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable Integer id){
        inMemoryFilmStorage.deleteFilm(id);
    }
}
