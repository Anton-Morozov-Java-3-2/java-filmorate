package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable("filmId") Long filmId) {
        Film film = filmService.getFilmById(filmId);
        log.info("Get film: " + film);
        return film;
    }

    @GetMapping("films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        List<Film> popularFilms = filmService.getPopularFilms(count);;
        log.info("Get popular films: " + popularFilms);
        return popularFilms;
    }

    @GetMapping("/films")
    public Collection<Film> getFilms(){
        Collection<Film> films = filmService.getAllFilms();
        log.info("Get all films: " + films);
        return films;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        Film newFilm = filmService.createFilm(film);
        log.info("Create film: " + newFilm);
        return newFilm;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film newFilm = filmService.updateFilm(film);
        log.info("Update film to " + newFilm);
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.addLikeFilm(id, userId);
        log.info(String.format("Film id=%d received a like from the user id=%d", id, userId));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.deleteLikeFilm(id, userId);
        log.info(String.format("User id=%d deleted the like of the film id=%s", userId, id));
    }
}
