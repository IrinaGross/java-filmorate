package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    @NonNull
    List<Genre> findAll();

    @NonNull
    Genre getById(@NonNull Long genreId);
}
