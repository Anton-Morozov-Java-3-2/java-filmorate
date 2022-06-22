package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film>  films;
    private final Map<Long, Set<Long>> likes;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
        this.likes = new HashMap<>();
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        if (films.containsKey(filmId)) {
            if (likes.containsKey(filmId)) {
                likes.get(filmId).add(userId);
            } else {
                likes.put(filmId, new HashSet<>());
            }
        } else {
            FilmNotFoundException exception = new FilmNotFoundException(FilmNotFoundException.
                    createMessage("Access error when adding like.",filmId));
            log.warn(exception.getMessage());
        }
    }

    @Override
    public void deleteLikeFilm(Long filmId, Long userId) {
        if (films.containsKey(filmId)) {
            if (likes.containsKey(filmId)) {
                likes.get(filmId).remove(userId);
            }
        } else {
            FilmNotFoundException exception = new FilmNotFoundException(FilmNotFoundException.
                    createMessage("Access error when delete like.",filmId));
            log.warn(exception.getMessage());
        }
    }

    @Override
    public Map<Long, Integer> getRatingFilms() {
        Map<Long, Integer> rating = new HashMap<>();
        for (Long idFilm : likes.keySet()) {
            rating.put(idFilm, likes.get(idFilm).size());
        }
        return rating;
    }

    @Override
    public Set<Long> getIdFilms() {
        return films.keySet();
    }

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)){
            return films.get(id);
        } else {
            FilmNotFoundException exception =
                    new FilmNotFoundException(FilmNotFoundException.createMessage("Access error.",id));
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void updateFilm(Film film) {
        Long id = film.getId();
        if (films.containsKey(id)) {
            films.replace(id, film);
        } else {
            FilmNotFoundException exception =
                    new FilmNotFoundException(FilmNotFoundException.createMessage("Update error.", id));
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
