package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {
    static FilmController filmController;

    @BeforeEach
    void initController() {
        filmController = new FilmController();
    }

    @Test
    void StandardActionCrateFilm() {
        Film film = new Film("The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        assertEquals(film, listFilm.get(0));
    }

    @Test
    void StandardActionCrateAndUpdateFilm() {
        Film film = new Film("The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        Film matrix = new Film("The Matrix", "Test update",
                LocalDate.of(1999,3, 24), 100);
        matrix.setId(createFilm.getId());

        Film updateFilm = filmController.updateFilm(matrix);

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        assertEquals(updateFilm, listFilm.get(0));
    }

    @Test
    void StandardActionCrateAndUpdateFilmErrorId() {
        Film film = new Film("The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        Film matrix = new Film("The Matrix", "Test update",
                LocalDate.of(1999,3, 24), 100);
        matrix.setId(100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.updateFilm(matrix));
    }

    @Test
    void StandardActionCrateAndGetFilms() {
        Film film1 = new Film("The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm1 =  filmController.createFilm(film1);
        film1.setId(createFilm1.getId());


        Film film2 = new Film("The Matrix 2", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm2 =  filmController.createFilm(film2);
        film2.setId(createFilm2.getId());

        Film film3 = new Film("The Matrix 3", "Test",
                LocalDate.of(1999,3, 24), 100);

        Film createFilm3 =  filmController.createFilm(film3);
        film3.setId(createFilm3.getId());

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        List.of(film1, film2, film3).toArray();

        Assertions.assertArrayEquals(new Film[]{film1, film2, film3}, listFilm.toArray());
    }

    @Test
    void testReleaseDateBeforeDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 27);
        Film film = new Film("Film Test", "Test", releaseDay, 100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("The release date of " +
                "the film can't be earlier than the birthday of the cinema", exception.getMessage());
    }

    @Test
    void testReleaseDateEqualDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 28);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film("Film Test", "Test", releaseDay, 100);

        Assertions.assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testReleaseDateAfterDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 29);
        Film film = new Film("Film Test", "Test", releaseDay, 100);

        Assertions.assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testReleaseDateNull() {
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film("Film Test", "Test", null, 100);
        Exception exception = Assertions.assertThrows(ValidationException.class ,()->filmController.createFilm(film));
        assertEquals("Release day can't be null", exception.getMessage());
    }

    @Test
    void testDescriptionLength201Char(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < 201; i++) {
            builder.append("a");
        }
        Film film = new Film("Film Test", builder.toString(), releaseDay, 100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie description can't be longer than 200 characters", exception.getMessage());
    }

    @Test
    void testDescriptionLength200Char(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < 200; i++) {
            builder.append("a");
        }
        Film film = new Film("Film Test", builder.toString(), releaseDay, 100);
        assertDoesNotThrow(()->filmController.createFilm(film));

    }

    @Test
    void testDescriptionBlank(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film("Film Test", "", releaseDay, 100);
        assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testDescriptionNull(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film("Film Test", null, releaseDay, 100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie description can't be null", exception.getMessage());
    }

    @Test
    void testNameNull(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(null, "Test", releaseDay, 100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie title can't be null", exception.getMessage());
    }

    @Test
    void testNameBlank(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film("", "Test", releaseDay, 100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie title can't be blank", exception.getMessage());
    }

    @Test
    void testDurationNegative(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Film film = new Film("Film", "Test", releaseDay, -100);
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Duration film can't be negative", exception.getMessage());
    }

    @Test
    void testDurationZero(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(0);
        Film film = new Film("Film Test", "Test", releaseDay, 100);
        assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testDurationPositive(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(1);
        Film film = new Film("Film Test", "Test", releaseDay, 100);
        assertDoesNotThrow(()->filmController.createFilm(film));
    }
}
