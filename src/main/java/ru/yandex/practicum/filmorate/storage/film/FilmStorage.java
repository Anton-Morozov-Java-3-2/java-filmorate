package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Film getFilmById(Long id);
    Collection<Film> getAllFilms();
    void addFilm(Film film);
    void updateFilm(Film film);
    void addLikeFilm(Long filmId, Long userId);
    void deleteLikeFilm(Long filmId, Long userId);
    Map<Long, Integer> getRatingFilms();
    Set<Long> getIdFilms();
}
