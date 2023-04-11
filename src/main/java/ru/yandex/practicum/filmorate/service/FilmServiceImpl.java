package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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
        return filmLikesStorage.getMostPopularFilms(limit == null ? DEFAULT_POPULAR_LIMIT : limit)
                .map(filmStorage::getFilm)
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
}
