package ru.yandex.practicum.filmorate.service;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    @NonNull
    List<Genre> getAllGenres();

    @NonNull
    Genre getGenre(@NonNull Long genreId);
}
