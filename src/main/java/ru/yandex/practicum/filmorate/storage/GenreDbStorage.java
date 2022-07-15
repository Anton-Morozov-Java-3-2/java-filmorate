package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT name FROM \"genre\" WHERE genre_id = ?;";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        if (genreRows.next()) {
            return new Genre(id, genreRows.getString("name"));
        } else {
            GenreNotFoundException exception = new GenreNotFoundException(GenreNotFoundException.
                    createMessage("Access error when get genre.", id));
            log.warn(exception.getMessage());
            throw  exception;
        }
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM \"genre\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public List<Genre> getGenresByFilmId(Long id) {
        String sql = "SELECT genre_id FROM \"film_genre\" WHERE film_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("genre_id"), id)
                .stream()
                .map(this::getGenreById)
                .collect(Collectors.toList());
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"),rs.getString("name"));
    }

    public void addFilmGenre(long filmId, int genreId) {
        validationExistsGenre(genreId);
        String sql = "MERGE INTO \"film_genre\" (film_id, genre_id) values (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    public void deleteFilmGenre(long filmId, int genreId) {
        validationExistsGenre(genreId);
        String sql_delete = "DELETE FROM \"film_genre\" WHERE film_id = ? and genre_id = ?";
        jdbcTemplate.update(sql_delete, filmId, genreId);
    }

    private void validationExistsGenre(int id) {
        getGenreById(id);
    }
}
