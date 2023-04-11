package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SuppressWarnings("unused")
@Slf4j
@RestController("/films")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmController {
    private final FilmService service;

    @GetMapping("/films")
    public List<Film> findAll() {
        List<Film> allFilms = service.getAllFilms();
        log.info("Текущее количество фильмов: {}", allFilms.size());
        return allFilms;
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") @NonNull Long filmId) {
        return service.getFilm(filmId);
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) {
        validate(film, false);
        service.addFilm(film);
        log.trace("Фильм добавлен");
        return film;
    }

    @PutMapping("/films")
    public Film put(@RequestBody @Valid Film film) {
        validate(film, true);
        service.updateFilm(film);
        log.trace("Фильм сохранен");
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(
            @PathVariable("id") @NonNull Long filmId,
            @PathVariable("userId") @NonNull Long userId
    ) {
        service.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void unlikeFilm(
            @PathVariable("id") @NonNull Long filmId,
            @PathVariable("userId") @NonNull Long userId
    ) {
        service.unlikeFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> topFilms(@RequestParam(value = "count", required = false) Integer limit) {
        return service.getMostPopularFilms(limit);
    }
}
