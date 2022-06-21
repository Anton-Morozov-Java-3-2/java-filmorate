package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String massage) {
        super(massage);
    }

    public static String createMassage(String massage, Long id) {
        return String.format("%s Film with id= %d not found.", massage, id);
    }
}
