package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ru.yandex.practicum.filmorate.utils.MapUtils.getOrEmptySet;

@Component
class InMemoryFilmLikesStorage implements FilmLikesStorage {
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrEmptySet(storage, filmId);
        collection.remove(userId);
        storage.put(filmId, collection);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrEmptySet(storage, filmId);
        collection.add(userId);
        storage.put(filmId, collection);
    }

    @Override
    public Map<Long, Set<Long>> getRating() {
        return Collections.unmodifiableMap(storage);
    }
}
