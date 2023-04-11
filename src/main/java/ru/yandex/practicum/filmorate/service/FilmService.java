package ru.yandex.practicum.filmorate.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    @NonNull
    List<Film> getAllFilms();

    @NonNull
    Film getFilm(@NonNull Long filmId);

    void updateFilm(@NonNull Film film);

    void addFilm(@NonNull Film film);

    @NonNull
    List<Film> getMostPopularFilms(@Nullable Integer limit);

    void unlikeFilm(@NonNull Long filmId, @NonNull Long userId);

    void likeFilm(@NonNull Long filmId, @NonNull Long userId);
}
