package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {
    static FilmController filmController;
    static UserService userService;

    @BeforeEach
    void initController() {
        userService = new UserService(new InMemoryUserStorage());
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), userService));
    }

    @Test
    void StandardActionCrateFilm() {
        Film film = new Film(1L,"The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100, 4,  null, new Mpa(1, ""));

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        assertEquals(film, listFilm.get(0));
    }

    @Test
    void StandardActionCrateAndUpdateFilm() {
        Film film = new Film(1L,"The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100, 4,  null, new Mpa(1, ""));

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        Film matrix = new Film(1L, "The Matrix", "Test Update",
                LocalDate.of(1999,3, 24), 100, 4,  null, new Mpa(1, ""));
        matrix.setId(createFilm.getId());

        Film updateFilm = filmController.updateFilm(matrix);

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        assertEquals(updateFilm, listFilm.get(0));
    }

    @Test
    void StandardActionCrateAndUpdateFilmErrorId() {
        Film film = new Film(1L, "The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100, 4,  null, new Mpa(1, ""));

        Film createFilm =  filmController.createFilm(film);
        film.setId(createFilm.getId());

        assertEquals(film, createFilm);

        Film matrix = new Film(1L, "The Matrix", "Test updateFilm",
                LocalDate.of(1999,3, 24), 100,4,  null, new Mpa(1, ""));
        matrix.setId(100);
        Exception exception = assertThrows(FilmNotFoundException.class, ()->filmController.updateFilm(matrix));
    }

    @Test
    void StandardActionCrateAndGetFilms() {
        Film film1 = new Film(1, "The Matrix", "Test",
                LocalDate.of(1999,3, 24), 100,4,  null, new Mpa(1, ""));

        Film createFilm1 =  filmController.createFilm(film1);
        film1.setId(createFilm1.getId());


        Film film2 = new Film(1L, "The Matrix 2", "Test",
                LocalDate.of(1999,3, 24), 100,4,  null, new Mpa(1, ""));

        Film createFilm2 =  filmController.createFilm(film2);
        film2.setId(createFilm2.getId());

        Film film3 = new Film(1, "The Matrix 3", "Test",
                LocalDate.of(1999,3, 24), 100, 4,  null, new Mpa(1, ""));

        Film createFilm3 =  filmController.createFilm(film3);
        film3.setId(createFilm3.getId());

        List<Film> listFilm = new ArrayList<>(filmController.getFilms());
        List.of(film1, film2, film3).toArray();

        Assertions.assertArrayEquals(new Film[]{film1, film2, film3}, listFilm.toArray());
    }

    @Test
    void testReleaseDateBeforeDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 27);
        Film film = new Film(1,"Film Test", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("The release date of " +
                "the film can't be earlier than the birthday of the cinema", exception.getMessage());
    }

    @Test
    void testReleaseDateEqualDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 28);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L, "Film Test", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));

        Assertions.assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testReleaseDateAfterDecember_28_1895() {
        LocalDate releaseDay = LocalDate.of(1895, 12, 29);
        Film film = new Film(1L, "Film Test", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));

        Assertions.assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testReleaseDateNull() {
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L,"Film Test", "Test", null, 100, 4,  null, new Mpa(1, ""));
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
        Film film = new Film(1L, "Film Test", builder.toString(), releaseDay, 100, 4,
               null, new Mpa(1, ""));
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
        Film film = new Film(1L, "Film Test", builder.toString(), releaseDay, 100, 4,
               null, new Mpa(1, ""));
        assertDoesNotThrow(()->filmController.createFilm(film));

    }

    @Test
    void testDescriptionBlank(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L, "Film Test", "", releaseDay, 100, 4, null, new Mpa(1, ""));
        assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testDescriptionNull(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L, "Film Test", null, releaseDay, 100, 4,  null, new Mpa(1, ""));
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie description can't be null", exception.getMessage());
    }

    @Test
    void testNameNull(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L,null, "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie title can't be null", exception.getMessage());
    }

    @Test
    void testNameBlank(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(100);
        Film film = new Film(1L, "", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Movie title can't be blank", exception.getMessage());
    }

    @Test
    void testDurationNegative(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Film film = new Film(1L,"Film", "Test", releaseDay, -100, 4,  null, new Mpa(1, ""));
        Exception exception = assertThrows(ValidationException.class, ()->filmController.createFilm(film));
        assertEquals("Duration film can't be negative", exception.getMessage());
    }

    @Test
    void testDurationZero(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(0);
        Film film = new Film(1L, "Film Test", "Test", releaseDay, 100,4,  null, new Mpa(1, ""));
        assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testDurationPositive(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(1);
        Film film = new Film(1L, "Film Test", "Test", releaseDay, 100,4,  null, new Mpa(1, ""));
        assertDoesNotThrow(()->filmController.createFilm(film));
    }

    @Test
    void testPopularFilms(){
        LocalDate releaseDay = LocalDate.of(1999, 12, 29);
        Duration duration = Duration.ofMinutes(0);
        Film film1 = new Film(1L,"Film1", "Test", releaseDay, 100,4,  null, new Mpa(1, ""));
        film1 = filmController.createFilm(film1);
        Film film2 = new Film(1L,"Film2", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        film2 = filmController.createFilm(film2);
        Film film3 = new Film(1L, "Film3", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        film3 = filmController.createFilm(film3);
        Film film4 = new Film(1L, "Film4", "Test", releaseDay, 100, 4,  null, new Mpa(1, ""));
        film4 = filmController.createFilm(film4);

        LocalDate birthday =  LocalDate.of(1976, 8, 20);
        User user1 = new User( 0,  "friend@mail.ru","friend1","friend adipisicing", birthday);
        User user2 = new User( 0,  "friend@mail.ru","friend2","friend adipisicing", birthday);
        User user3 = new User( 0,  "friend@mail.ru","friend3","friend adipisicing", birthday);
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        user3 = userService.createUser(user3);

        filmController.addLike(film2.getId(), user1.getId());
        filmController.addLike(film2.getId(), user2.getId());
        filmController.addLike(film2.getId(), user3.getId());

        filmController.addLike(film3.getId(), user1.getId());
        filmController.addLike(film3.getId(), user2.getId());

        List<Film> expected = List.of(film2, film3, film4, film1);
        List<Film> popular = filmController.getPopularFilms(20);
        assertArrayEquals(expected.toArray(), popular.toArray());
    }
}
