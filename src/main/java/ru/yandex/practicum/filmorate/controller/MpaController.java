package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
public class MpaController {
    private final FilmService filmService;

    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable("mpaId") Integer mpaId) {
        Mpa mpa = filmService.getMpaById(mpaId);
        log.info("Get mpa: " + mpa);
        return mpa;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa(){
        List<Mpa> mpa = filmService.getAllMpa();
        log.info("Get all mpa: " + mpa);
        return mpa;
    }
}
