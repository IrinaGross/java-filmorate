package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class InMemoryFilmStorage implements FilmStorage {
    private long id;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addFilm(Film film) {
        long id = ++this.id;
        film.setId(id);
        films.put(id, film);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
    }

    @Override
    public Film getFilm(Long filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }
}
