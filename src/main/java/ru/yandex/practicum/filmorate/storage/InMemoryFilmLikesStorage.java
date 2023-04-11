package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class InMemoryFilmLikesStorage implements FilmLikesStorage {
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    @Override
    public Stream<Long> getMostPopularFilms(Integer count) {
        return storage.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Set::size)))
                .filter(it -> !it.getValue().isEmpty())
                .limit(count)
                .map(Map.Entry::getKey);
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrDefault(filmId);
        collection.remove(userId);
        storage.put(filmId, collection);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrDefault(filmId);
        collection.add(userId);
        storage.put(filmId, collection);
    }

    private Set<Long> getOrDefault(Long filmId) {
        return storage.getOrDefault(filmId, new HashSet<>());
    }
}
