package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Slf4j
@RestController("/films")
public class FilmController {
    private static final int MAX_LENGTH = 200;
    private static final int MIN_DURATION = 0;
    private final static LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private long id;
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        validate(film, false);
        long id = ++this.id;
        film.setId(id);
        films.put(id, film);
        log.trace("Фильм добавлен");
        return film;
    }

    @PutMapping("/films")
    public Film put(@RequestBody Film film) {
        validate(film, true);
        if (!films.containsKey(film.getId())) {
            throw  new ValidationException("Такого фильма нет");
        }
        films.put(film.getId(), film);
        log.trace("Фильм сохранен");
        return film;
    }

    static void validate(Film film, Boolean checkId) {
        if ((checkId && Objects.isNull(film.getId()))
                || Objects.isNull(film.getName()) || film.getName().isEmpty()
                || Objects.isNull(film.getDescription()) || film.getDescription().isEmpty() || film.getDescription().length() > MAX_LENGTH
                || Objects.isNull(film.getReleaseDate()) || film.getReleaseDate().isBefore(RELEASE_DATE) || film.getReleaseDate().isAfter(LocalDate.now())
                || Objects.isNull(film.getDuration()) || film.getDuration() <= MIN_DURATION) {
            log.warn("Фильм не сохранен из-за некорректных данных");
            throw new ValidationException("Некорректные данные фильма");
        }
    }
}
