package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.checkExist;
import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SuppressWarnings("unused")
@Slf4j
@RestController("/films")
public class FilmController {

    private long id;
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) {
        validate(film, false);
        long id = ++this.id;
        film.setId(id);
        films.put(id, film);
        log.trace("Фильм добавлен");
        return film;
    }

    @PutMapping("/films")
    public Film put(@RequestBody @Valid Film film) {
        validate(film, true);
        checkExist(films, film);
        films.put(film.getId(), film);
        log.trace("Фильм сохранен");
        return film;
    }
}
