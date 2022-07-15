package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException(String message) {
        super(message);
    }

    public static String createMessage(String message, Integer id) {
        return String.format("%s Genre with id= %d not found.", message, id);
    }
}
