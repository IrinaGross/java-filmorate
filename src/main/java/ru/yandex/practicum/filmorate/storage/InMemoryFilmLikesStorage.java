package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class InMemoryFilmLikesStorage implements FilmLikesStorage {
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrDefault(storage, filmId);
        collection.remove(userId);
        storage.put(filmId, collection);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Set<Long> collection = getOrDefault(storage, filmId);
        collection.add(userId);
        storage.put(filmId, collection);
    }

    @Override
    public Comparator<? super Film> ratingComparator() {
        return new FilmComparator(storage);
    }

    static class FilmComparator implements Comparator<Film> {
        private final Map<Long, Set<Long>> snapshot;

        public FilmComparator(Map<Long, Set<Long>> map) {
            snapshot = new HashMap<>(map);
        }

        @Override
        public int compare(Film o1, Film o2) {
            int o1LikesCount = getOrDefault(snapshot, o1.getId()).size();
            int o2LikesCount = getOrDefault(snapshot, o2.getId()).size();
            return Integer.compare(o2LikesCount, o1LikesCount);
        }
    }

    private static Set<Long> getOrDefault(Map<Long, Set<Long>> storage, Long filmId) {
        return storage.getOrDefault(filmId, new HashSet<>());
    }
}
