package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Set;

public interface FilmLikesStorage {
    void unlikeFilm(@NonNull Long filmId, @NonNull Long userId);

    void likeFilm(@NonNull Long filmId, @NonNull Long userId);

    @NonNull
    Map<Long, Set<Long>> getRating();
}
