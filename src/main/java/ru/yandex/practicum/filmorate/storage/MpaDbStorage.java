package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sql = "SELECT name FROM \"mpa\" WHERE id = ?;";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql, id);
        if (mpaRows.next()) {
            return new Mpa(id, mpaRows.getString("name"));
        } else {
            String message = String.format("Mpa rating with id=%s not found", id);
            log.warn(message);
            throw new MpaNotFoundException(message);
        }
    }

    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM \"mpa\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    public Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa(rs.getInt("id"), rs.getString("name"));
        return mpa;
    }
}
