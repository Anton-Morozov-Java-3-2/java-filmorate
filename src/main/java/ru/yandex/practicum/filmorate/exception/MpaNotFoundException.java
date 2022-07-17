package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends RuntimeException{
        public MpaNotFoundException(String message) {
            super(message);
        }

        public static String createMessage(String message, Integer id) {
            return String.format("%s Rating MPA with id= %d not found.", message, id);
        }
}

