package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Film getFilmById(Long id);
    Collection<Film> getAllFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void addLikeFilm(Long filmId, Long userId);
    void deleteLikeFilm(Long filmId, Long userId);
    Map<Long, Integer> getRatingFilms();
    Set<Long> getIdFilms();
    Genre getGenreById(int id);
    List<Genre> getAllGenre();
    Mpa getMpaById(int id);
    List<Mpa> getAllMpa();
}
