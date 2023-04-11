package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;

import java.util.stream.Stream;

public interface FilmLikesStorage {
    Stream<Long> getMostPopularFilms(@NonNull Integer count);

    void unlikeFilm(@NonNull Long filmId, @NonNull Long userId);

    void likeFilm(@NonNull Long filmId, @NonNull Long userId);
}
