package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("MpaDbStorage")
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM films_ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> uploadMpa(rs, rowNum));
    }

    @Override
    public Optional<Mpa> findMpaById(Integer id) {
        String sql = "SELECT * FROM films_ratings WHERE rating_id = ?";
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql , id);
        if (sqlRow.next()){
            Mpa mpa = new Mpa(
                    sqlRow.getInt("rating_id"),
                    sqlRow.getString("rating_name"));
            return Optional.of(mpa);
        } else {
            throw new FilmUserNotFoundException("Нет такого MPA");
        }
    }

    private Mpa uploadMpa(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() != 0) {
            return new Mpa(rs.getInt("rating_id"), rs.getString("rating_name"));
        } else {
            throw new FilmUserNotFoundException("Нет MPA в базе");
        }
    }
}
