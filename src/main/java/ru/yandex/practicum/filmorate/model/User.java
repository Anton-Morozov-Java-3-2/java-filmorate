package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private long id;

    @NotNull
    @NotBlank
    @Email
    private final String email;

    @NotNull
    @NotBlank
    private final String login;
    private String name;

    @NotNull
    @Past
    private final LocalDate birthday;
}
