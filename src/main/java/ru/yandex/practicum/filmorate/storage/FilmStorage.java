package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    @NonNull
    List<Film> getAllFilms();

    void addFilm(@NonNull Film film);

    void updateFilm(@NonNull Film film);

    @NonNull
    Film getFilm(@NonNull Long filmId);
}
