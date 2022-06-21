package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private long counterId = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private long createId() {
        return ++counterId;
    }

    public List<Film> getPopularFilms(Integer size) {
        Map<Long, Integer> rating = filmStorage.getRatingFilms();
        Set<Long> idFilms = filmStorage.getIdFilms();
        return new ArrayList<>(idFilms)
                .stream()
                .sorted((idFilms1, idFilms2) -> compare(idFilms1,idFilms2, rating))
                .limit(size)
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    private int compare(Long p0, Long p1, Map<Long, Integer> rating) {
        if (rating.containsKey(p0) && rating.containsKey(p1)) return rating.get(p1) - rating.get(p0);
        if (rating.containsKey(p0)) return -1;
        if (rating.containsKey(p1)) return 1;
        return (int) (p1 - p0);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        validateFilm(film, "create");
        long id = createId();
        film.setId(id);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film, "update");
        filmStorage.updateFilm(film);
        return film;
    }

    public void addLikeFilm(Long filmId, Long userId) {
        if (userService.isExistUser(userId)) {
            filmStorage.addLikeFilm(filmId, userId);
        } else {
            String massage = String.format("User with id=%s not found", userId);
            log.warn(massage);
            throw new UserNotFoundException(massage);
        }
    }

    public void deleteLikeFilm(Long filmId, Long userId) {
        if (userService.isExistUser(userId)) {
            filmStorage.deleteLikeFilm(filmId, userId);
        } else {
            String massage = String.format("User with id=%s not found", userId);
            log.warn(massage);
            throw new UserNotFoundException(massage);
        }
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

        if (film == null) throw new ValidationException("Object film can't be null", "object");

        // check name
        if (film.getName() == null) throw new ValidationException("Movie title can't be null", "title");
        if (film.getName().isBlank()) throw new ValidationException("Movie title can't be blank", "title");

        //check description
        if (film.getDescription() == null) throw  new ValidationException("Movie description can't be null", "description");
        if (film.getDescription().length() > 200) throw new ValidationException("Movie description can't be " +
                "longer than " +
                "200 characters", "description");

        //check release data
        if (film.getReleaseDate() == null) throw new ValidationException("Release day can't be null", "releaseDate");
        if (film.getReleaseDate().isBefore(Film.BIRTHDAY_CINEMA)) throw new ValidationException("The release date of " +
                "the film can't be earlier than the birthday of the cinema", "releaseDate");

        //check duration
        if (film.getDuration() < 0) throw new ValidationException("Duration film can't be negative", "duration");
    }
}
