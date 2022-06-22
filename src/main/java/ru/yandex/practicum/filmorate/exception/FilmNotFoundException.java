package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super(message);
    }

    public static String createMessage(String message, Long id) {
        return String.format("%s Film with id= %d not found.", message, id);
    }
}
