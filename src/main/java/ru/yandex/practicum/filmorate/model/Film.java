package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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

    @NotNull
    private final int rate;

    @NotNull
    private final List<Genre> genres;

    @NotNull
    private final Mpa mpa;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, int rate,
                List<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = Objects.requireNonNullElseGet(genres, ArrayList::new);
        this.mpa = mpa;
    }

}
