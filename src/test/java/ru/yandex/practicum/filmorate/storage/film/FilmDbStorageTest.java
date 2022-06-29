package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    @Qualifier("filmDbStorage")
    @Autowired
    private FilmStorage filmStorage;

    Film film = new Film(
            "Фиксики",
            "мульт",
            LocalDate.of(2015, 10, 10),
            200,
            new Mpa(1, null)
    );
    Film filmNew = new Film(
            "ФиксикиNew",
            "мульт",
            LocalDate.of(2015, 10, 10),
            200,
            new Mpa(1, null)
    );

    Film filmPass = new Film(
            "Фиксики",
            "мульт",
            LocalDate.of(1700, 10, 10),
            200,
            new Mpa(1, null)
    );
    Film filmChange = new Film(
            "Смешарики",
            "мульт",
            LocalDate.of(1950, 10, 10),
            200,
            new Mpa(1, null)
    );

    @Test
    void addFilm() {
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(filmPass));
        assertTrue(filmStorage.addFilm(film).getId() != null);
    }

    @Test
    void deleteFilm() {
        filmStorage.deleteFilm(filmStorage.addFilm(film).getId());
        assertFalse(filmStorage.getAllFilms().contains(film));
    }

    @Test
    void changeFilm() {
        assertTrue(filmStorage.addFilm(film).getName().equals("Фиксики"));
        filmChange.setId(1);
        assertTrue(filmStorage.changeFilm(filmChange).getName().equals("Смешарики"));
    }

    @Test
    void getAllFilms() {
        filmStorage.addFilm(film);
        assertFalse(filmStorage.getAllFilms().isEmpty());
    }

    @Test
    void findFilmById() {
        Film film1 = filmStorage.addFilm(filmNew);
        assertTrue(filmStorage.findFilmById(film1.getId()).getName().equals(film1.getName()));
    }
}