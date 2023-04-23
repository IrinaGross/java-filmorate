package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    @Qualifier("db")
    private final GenreStorage storage;

    @Override
    @NonNull
    public List<Genre> getAllGenres() {
        return storage.findAll();
    }

    @Override
    @NonNull
    public Genre getGenre(@NonNull Long genreId) {
        return storage.getById(genreId);
    }
}
