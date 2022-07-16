package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.*;

@Component
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM \"film\" WHERE film_id = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    genreDbStorage.getGenresByFilmId(id),
                    mpaDbStorage.getMpaById(filmRows.getInt("mpa_id"))
            );

            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            FilmNotFoundException exception =
                    new FilmNotFoundException(FilmNotFoundException.createMessage("Access error.",id));
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT film_id, name, description, release_date, duration, rate, mpa_id  " +
                "FROM \"film\" ORDER BY film_id ASC;";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Film(
                        rs.getLong("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        rs.getInt("rate"),
                        genreDbStorage.getGenresByFilmId(rs.getLong("film_id")),
                        mpaDbStorage.getMpaById(rs.getInt("mpa_id")))
                );
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO \"film\" (film_id, name, description, release_date, duration, rate, " +
                "mpa_id) values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRate(),
                    mpaDbStorage.getMpaById(film.getMpa().getId()).getId()
                    );

        validateExistsFilm(film.getId());
        film.getGenres().forEach((g) -> genreDbStorage.addFilmGenre(film.getId(), g.getId()));

        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "MERGE INTO \"film\" (film_id, name, description, release_date, duration, rate, " +
                "mpa_id) values (?, ?, ?, ?, ?, ?, ?)";
        validateExistsFilm(film.getId());
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                mpaDbStorage.getMpaById(film.getMpa().getId()).getId()
        );

        validateExistsFilm(film.getId());
        List<Genre> old_genre = genreDbStorage.getGenresByFilmId(film.getId());
        List<Genre> new_genre = film.getGenres();

        old_genre.forEach((g)-> genreDbStorage.deleteFilmGenre(film.getId(), g.getId()));
        new_genre.forEach((g)->genreDbStorage.addFilmGenre(film.getId(), g.getId()));
        return getFilmById(film.getId());
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        validateExistsFilm(filmId);
        String sql = "INSERT INTO \"like\" (film_id, user_id)  VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLikeFilm(Long filmId, Long userId) {
        validateExistsFilm(filmId);
        String sql = "DELETE FROM \"like\" WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Map<Long, Integer> getRatingFilms() {
        String sql = "SELECT film_id, COUNT(user_id) FROM \"like\" GROUP BY film_id;";
        Map<Long, Integer> result = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            result.put(Long.parseLong(map.get("FILM_ID").toString()),
                    Integer.parseInt(map.get("COUNT(USER_ID)").toString()));
        }
        return result;
    }

    @Override
    public Set<Long> getIdFilms() {
        String sql = "SELECT film_id FROM \"film\";";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id")));
    }

    private void validateExistsFilm(Long id) {
        getFilmById(id);
    }

    @Override
    public Genre getGenreById(int id) {
        return genreDbStorage.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreDbStorage.getAllGenres();
    }

    @Override
    public Mpa getMpaById(int id) {
        return mpaDbStorage.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }
}
