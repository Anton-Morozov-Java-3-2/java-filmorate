package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895,12,28);

    private long id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @Size(max = 200, message = "description length cannot be more then 200 characters")
    private final String description;

    @NotNull
    private final LocalDate releaseDate;

    @NotNull
    @Positive
    private final int duration;
}
