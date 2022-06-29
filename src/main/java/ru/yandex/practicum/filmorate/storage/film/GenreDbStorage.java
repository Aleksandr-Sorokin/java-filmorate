package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> uploadGenre(rs, rowNum)));
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return Optional.of(new Genre(rowSet.getInt("genre_id"),
                    rowSet.getString("genre_name")));
        } else {
            throw new FilmUserNotFoundException("Нет GENRE в базе id " + id);
        }
    }

    private Genre uploadGenre(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() != 0) {
            return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        } else {
            throw new FilmUserNotFoundException("Нет GENRE в базе");
        }
    }
}
