package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    @Override
    public Film addFilm(Film film) {
        if (FilmStorage.checkValidationFilm(film, log)){
        Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingColumns("film_name", "film_description",
                        "film_release_date", "film_duration", "film_rating")
                .usingGeneratedKeyColumns("film_id")
                .executeAndReturnKeyHolder(Map.of("film_name", film.getName(),
                        "film_description", film.getDescription(),
                        "film_release_date", Date.valueOf(film.getReleaseDate()),
                        "film_duration", film.getDuration(),
                        "film_rating", film.getMpa().getId()))
                .getKeys();
        film.setId((Integer) keys.get("film_id"));
        film.setMpa(mpaService.findMpaById(film.getMpa().getId()));
        updateGenres(film);
        return film;
        } else {
            throw new ValidationException("Фильм не добавлен");
        }
    }

    private void updateGenres(Film film) {
        String deleteGenre = "DELETE FROM films_genres WHERE film_id = ?";
        String insertGenre = "INSERT INTO films_genres (film_id, genre_film) VALUES (?, ?)";
        jdbcTemplate.update(deleteGenre, film.getId());
        if (film.getGenres() != null) {
            film.getGenres().stream()
                    .map(Genre::getId)
                    .forEach(idGenre -> jdbcTemplate.update(insertGenre, film.getId(), idGenre));
        }
    }

    private void updateLikes(Film film) {
        String deleteLikes = "DELETE FROM films_likes WHERE film_id = ?";
        String insertLikes = "INSERT INTO films_likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(deleteLikes, film.getId());
        film.getLike().stream()
                .forEach(idUser -> jdbcTemplate.update(insertLikes, idUser, film.getId()));
    }

    @Override
    public void deleteFilm(Integer idFilm) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql,idFilm);
    }

    @Override
    public Film changeFilm(Film film) {
        if (FilmStorage.checkValidationFilm(film, log)){
            checkFilmById(film.getId());
            String updateFilms = "UPDATE films SET film_name = ?, film_description = ?," +
                    " film_release_date = ?, film_duration = ?, film_rating = ?" +
                    " WHERE film_id = ?";
            jdbcTemplate.update(updateFilms,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            updateGenres(film);
            updateLikes(film);
            return findFilmById(film.getId());
        } else {
            throw new FilmUserNotFoundException("Фильм небыл изменен");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> uploadFilm(rs, rowNum));
    }

    @Override
    public Film findFilmById(int idFilm) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql , idFilm);
        if (sqlRow.next()){
            Film film = new Film(
                    sqlRow.getString("film_name"),
                    sqlRow.getString("film_description"),
                    sqlRow.getDate("film_release_date").toLocalDate(),
                    sqlRow.getInt("film_duration"),
                    mpaService.findMpaById(sqlRow.getInt("film_rating"))
                    );
            film.setId(sqlRow.getInt("film_id"));
            addGenreForFilm(film);
            addLikeForFilm(film);
            return film;
        } else {
            throw new FilmUserNotFoundException("Нет такого Film");
        }
    }

    private void addGenreForFilm(Film film){
        String sql = "SELECT genre_name, genre_id from genres g " +
                "LEFT JOIN films_genres fg on fg.genre_film = g.genre_id " +
                "where film_id=?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql,
                (rs, num) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")),
                film.getId())
        );
        film.setGenres(genres);
    }

    private void addLikeForFilm(Film film){
        String sqlLike = "SELECT * FROM films_likes WHERE film_id = ?";
        List<Integer> idUser = jdbcTemplate.query(sqlLike, ((rs, rowNum) -> {
            if (rs.getRow() != 0) {
                return rs.getInt("user_id");
            } else {
                throw new FilmUserNotFoundException("Нет LIKES");
            }
        }), film.getId());
        Set<Integer> likes = new HashSet<>();
        for (Integer l: idUser){
            likes.add(l);
        }
        film.setLike(likes);
    }

    public void checkFilmById(int idFilm) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql , idFilm);
        if (!sqlRow.next()){
            throw new FilmUserNotFoundException("Нет такого film id");
        }
    }

    private Film uploadFilm(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() != 0) {
            Film film = new Film(rs.getString("film_name"),
                    rs.getString("film_description"),
                    rs.getDate("film_release_date").toLocalDate(),
                    rs.getInt("film_duration"),
                    mpaService.findMpaById(rs.getInt("film_rating"))
            );
            film.setId(rs.getInt("film_id"));
            addLikeForFilm(film);
            addGenreForFilm(film);
            return film;
        } else {
            throw new FilmUserNotFoundException("Нет MPA в базе");
        }
    }
}
