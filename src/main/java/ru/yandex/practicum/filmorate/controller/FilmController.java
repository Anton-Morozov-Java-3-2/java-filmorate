package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private long counterId = 0;

    private long createId() {
        return ++counterId;
    }

    @GetMapping
    public Collection<Film> getFilms(){
        return  films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film, "create");
        long id = createId();
        film.setId(id);
        films.put(id, film);
        log.info("Add film: " + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateFilm(film, "update");
        long id = film.getId();
        if (films.containsKey(id)) {
            Film old = films.get(id);
            films.replace(id, film);
            log.info("Update film: " + old + " to " + film);
            return film;
        }
        final String massage = "Update request error film. " + film + " not contains!";
        log.warn(massage);
        throw new ValidationException(massage);
    }

    private static void validateFilm(Film film, String operation) {
        try {
            checkFilmData(film);
        } catch (ValidationException e) {
            String massage = "Error " + operation + ". Validation exception thrown: " + e.getMessage() +
                    ". Film data: " +  film;
            log.warn(massage);
            throw e;
        }
    }

    private static void checkFilmData(Film film) {
        final int MAX_LENGTH = 200;

        if (film == null) throw new ValidationException("Object film can't be null");

        // check name
        if (film.getName() == null) throw new ValidationException("Movie title can't be null");
        if (film.getName().isBlank()) throw new ValidationException("Movie title can't be blank");

        //check description
        if (film.getDescription() == null) throw  new ValidationException("Movie description can't be null");
        if (film.getDescription().length() > 200) throw new ValidationException("Movie description can't be " +
                    "longer than " +
                    "200 characters");

        //check release data
        if (film.getReleaseDate() == null) throw new ValidationException("Release day can't be null");
        if (film.getReleaseDate().isBefore(Film.BIRTHDAY_CINEMA)) throw new ValidationException("The release date of " +
                "the film can't be earlier than the birthday of the cinema");

        //check duration
        if (film.getDuration() < 0) throw new ValidationException("Duration film can't be negative");
    }
}
