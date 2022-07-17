package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
public class GenreController {

    private final FilmService filmService;

    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable("genreId") Integer genreId) {
        Genre genre = filmService.getGenreById(genreId);
        log.info("Get genre: " + genre);
        return genre;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres(){
        List<Genre> genres = filmService.getAllGenres();
        log.info("Get all genres: " + genres);
        return genres;
    }
}
