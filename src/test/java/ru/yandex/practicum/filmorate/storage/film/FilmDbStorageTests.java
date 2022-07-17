package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmDbStorage.getAllFilms();

        Genre genre1 = new Genre(6, "Боевик");
        Mpa mpa1 = new Mpa(4, "R");
        Film film1 = new Film(1, "Матрица",
                "Добро пожаловать в реальный мир", LocalDate.of(1999, 3, 24), 136, 4,
                List.of(genre1), mpa1);

        Genre genre2 = new Genre(2, "Драма");
        Mpa mpa2 = new Mpa(3, "PG-13");
        Film film2 = new Film(2, "Форрет Гамп",
                "Полувековая история США глазами чудака из Алабамы.", LocalDate.of(1994, 6, 23), 142, 4,
                List.of(genre2), mpa2);

        Genre genre3 = new Genre(4,"Триллер");
        Mpa mpa3 = new Mpa(1, "G");
        Film film3 = new Film(3, "2001 год: Космическая одиссея",
                "the time is now", LocalDate.of(1968, 4, 2), 149, 4,
                List.of(genre3), mpa3);

        assertArrayEquals(films.toArray(), new Film[]{film1, film2, film3});
    }

    @Test
    public void testGetFilmById() {
        Film film = filmDbStorage.getFilmById(1L);

        Genre genre1 = new Genre(6,"Боевик");
        Mpa mpa1 = new Mpa(4, "R");
        Film film1 = new Film(1, "Матрица",
                "Добро пожаловать в реальный мир", LocalDate.of(1999, 3, 24), 136, 4,
                List.of(genre1), mpa1);

        assertEquals(film, film1);
    }

    @Test
    public void testGetIdFilms() {
        Set<Long> idFilms = filmDbStorage.getIdFilms();
        assertArrayEquals(idFilms.toArray(), List.of(1L, 2L, 3L).toArray());
    }

    @Test
    public void  testGetRatingFilms() {
        Map<Long, Integer> result = filmDbStorage.getRatingFilms();
        assertEquals(result.get(1L), 2);
        assertEquals(result.get(2L), 1);
    }

    @Test
    public void  testAddFilm() {
        Genre genre = new Genre(4, "Триллер");
        Mpa mpa = new Mpa(1, "G");

        Film film = new Film(4L, "Test", "Test add",
                LocalDate.of(2022, 7, 13), 4, 4,
                List.of(genre), mpa);

        filmDbStorage.addFilm(film);
        assertEquals(film, filmDbStorage.getFilmById(4L));
        jdbcTemplate.update("DELETE FROM \"film_genre\" WHERE film_id = ?;", film.getId());
        jdbcTemplate.update("DELETE FROM \"like\" WHERE film_id = ?;", film.getId());
        jdbcTemplate.update("DELETE FROM \"film\" WHERE film_id = ?;", film.getId());
    }

    @Test
    public void  testUpdateFilm() {
        Genre genre = new Genre(4, "Триллер");
        Mpa mpa = new Mpa(1, "G");

        Film film = new Film(4L, "Test", "Test add",
                LocalDate.of(2022, 7, 13), 4, 4,
                List.of(genre), mpa);
        filmDbStorage.addFilm(film);

        Mpa mpa2 = new Mpa(2, "PG");
        Genre genre2 = new Genre(6, "Боевик");

        Film new_film = new Film(4L, "Test Update", "Test add",
                LocalDate.of(2022, 7, 13), 4, 4,
                List.of(genre2), mpa2);
        filmDbStorage.updateFilm(new_film);


        assertEquals(new_film, filmDbStorage.getFilmById(4L));
        jdbcTemplate.update("DELETE FROM \"film_genre\" WHERE film_id = ?;", new_film.getId());
        jdbcTemplate.update("DELETE FROM \"like\" WHERE film_id = ?;", new_film.getId());
        jdbcTemplate.update("DELETE FROM \"film\" WHERE film_id = ?;", new_film.getId());
    }
}
