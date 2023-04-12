package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.utils.MapUtils.getOrEmptySet;

@Service
@RequiredArgsConstructor
class FilmServiceImpl implements FilmService {
    public static final int DEFAULT_POPULAR_LIMIT = 10;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmLikesStorage filmLikesStorage;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }

    @Override
    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    @Override
    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer limit) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted(new FilmComparator(filmLikesStorage.getRating()))
                .limit(limit == null ? DEFAULT_POPULAR_LIMIT : limit)
                .collect(Collectors.toList());
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmLikesStorage.unlikeFilm(film.getId(), user.getId());
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmLikesStorage.likeFilm(film.getId(), user.getId());
    }

    @RequiredArgsConstructor
    private static class FilmComparator implements Comparator<Film> {
        private final Map<Long, Set<Long>> snapshot;

        @Override
        public int compare(Film o1, Film o2) {
            int o1LikesCount = getOrEmptySet(snapshot, o1.getId()).size();
            int o2LikesCount = getOrEmptySet(snapshot, o2.getId()).size();
            return Integer.compare(o2LikesCount, o1LikesCount);
        }
    }
}
