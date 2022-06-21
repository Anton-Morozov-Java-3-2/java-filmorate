package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    public Film getFilmById(Long id);
    public Collection<Film> getAllFilms();
    public void addFilm(Film film);
    public void updateFilm(Film film);
    public void addLikeFilm(Long filmId, Long userId);
    public void deleteLikeFilm(Long filmId, Long userId);
    public Map<Long, Integer> getRatingFilms();
    public Set<Long> getIdFilms();
}
