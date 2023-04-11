package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public interface FilmLikesStorage {
    void unlikeFilm(@NonNull Long filmId, @NonNull Long userId);

    void likeFilm(@NonNull Long filmId, @NonNull Long userId);

    Comparator<? super Film> ratingComparator();
}
