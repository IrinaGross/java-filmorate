package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.Mappers.FILM_MAPPER;
import static ru.yandex.practicum.filmorate.storage.Mappers.GENRE_MAPPER;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @NonNull
    public List<Film> getAllFilms() {
        String query = "SELECT f.\"film_id\", f.\"film_name\", f.\"film_desc\",f.\"film_duration\" ,f.\"film_release_date\",m.\"mpa_id\" ,m.\"mpa_name\" " +
                "FROM \"filmorate\".\"film\" f " +
                "LEFT JOIN \"filmorate\".\"mpa\" m ON m.\"mpa_id\" = f.\"film_mpa\"";
        List<Film> films = jdbcTemplate.query(query, FILM_MAPPER);
        return films.stream()
                .peek(item -> item.setGenres(getGenresFor(item)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addFilm(@NonNull Film film) {
        checkRatingId(film.getMpa());
        checkGenres(film.getGenres());

        Long filmId = insertFilm(film);
        film.setId(filmId);
        linkFilmWithGenres(film, film.getGenres());
    }

    @Override
    @Transactional
    public void updateFilm(@NonNull Film film) {
        checkFilmId(film.getId());
        checkRatingId(film.getMpa());

        updateFilmInternal(film);
        updateFilmGenres(film);
    }

    @Override
    @NonNull
    public Film getFilm(@NonNull Long filmId) {
        String query = "SELECT f.\"film_id\", f.\"film_name\", f.\"film_desc\",f.\"film_duration\" ,f.\"film_release_date\",m.\"mpa_id\" ,m.\"mpa_name\" " +
                "FROM \"filmorate\".\"film\" f " +
                "LEFT JOIN \"filmorate\".\"mpa\" m ON m.\"mpa_id\" = f.\"film_mpa\" " +
                "WHERE f.\"film_id\" = ?";
        try {
            Film film = Objects.requireNonNull(jdbcTemplate.queryForObject(query, FILM_MAPPER, filmId));
            film.setGenres(getGenresFor(film));
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Фильм с идентификатором %1$d не найден", filmId), e);
        }
    }

    private void checkFilmId(@Nullable Long filmId) {
        if (filmId == null) {
            throw new NotFoundException(String.format("Фильм с идентификатором %1$d не найден", filmId));
        }
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) " +
                    "FROM \"filmorate\".\"film\" " +
                    "WHERE \"film_id\" = ?", Integer.class, filmId);
            if (count == null || count == 0) {
                throw new NotFoundException(String.format("Фильм с идентификатором %1$d не найден", filmId));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Фильм с идентификатором %1$d не найден", filmId), e);
        }
    }

    private void checkRatingId(@Nullable MpaRating rating) {
        if (rating == null) {
            return;
        }
        Long id = rating.getId();
        if (id == null) {
            throw new NotFoundException(String.format("Запись рейтинга ассоциации кинокомпаний с идентификатором %1$d не найдена", id));
        }
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) " +
                    "FROM \"filmorate\".\"mpa\" " +
                    "WHERE \"mpa_id\" = ?", Integer.class, id);
            if (count == null || count == 0) {
                throw new NotFoundException(String.format("Запись рейтинга ассоциации кинокомпаний с идентификатором %1$d не найдена", id));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Запись рейтинга ассоциации кинокомпаний с идентификатором %1$d не найдена", id), e);
        }
    }

    private void checkGenres(@Nullable List<Genre> genres) {
        if (genres == null) {
            return;
        }

        genres.forEach(item -> {
            Long id = item.getId();
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) " +
                        "FROM \"filmorate\".\"genre\" " +
                        "WHERE \"genre_id\" = ?", Integer.class, id);
                if (count == null || count == 0) {
                    throw new NotFoundException(String.format("Жанр с идентификатором %1$d не найден", id));
                }
            } catch (EmptyResultDataAccessException e) {
                throw new NotFoundException(String.format("Жанр с идентификатором %1$d не найден", id), e);
            }
        });
    }

    private void linkFilmWithGenres(@NonNull Film film, @Nullable List<Genre> genres) {
        if (genres == null) {
            return;
        }

        genres.forEach(item -> {
            String query = "INSERT INTO \"filmorate\".\"film_genre\" (\"film_id\", \"genre_id\") " +
                    "VALUES(?, ?)";
            jdbcTemplate.update(query, film.getId(), item.getId());
        });
        film.setGenres(genres);
    }

    @NonNull
    private Long insertFilm(@NonNull Film film) {
        String query = "INSERT INTO \"filmorate\".\"film\" (\"film_name\", \"film_desc\", \"film_duration\", \"film_release_date\", \"film_mpa\") " +
                "VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setLong(3, film.getDuration());
            statement.setObject(4, film.getReleaseDate());
            MpaRating mpa = film.getMpa();
            if (mpa != null) {
                statement.setLong(5, mpa.getId());
            } else {
                statement.setNull(5, Types.BIGINT);
            }
            return statement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKeyAs(Long.class));
    }

    private void updateFilmInternal(@NonNull Film film) {
        String query = "UPDATE \"filmorate\".\"film\" " +
                "SET \"film_name\" = ?, \"film_desc\" = ?, \"film_duration\" = ?, \"film_release_date\" = ?, \"film_mpa\" = ? " +
                "WHERE \"film_id\" = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setLong(3, film.getDuration());
            statement.setObject(4, film.getReleaseDate());
            MpaRating mpa = film.getMpa();
            if (mpa != null) {
                statement.setLong(5, mpa.getId());
            } else {
                statement.setNull(5, Types.BIGINT);
            }
            statement.setLong(6, film.getId());
            return statement;
        });
    }

    private void updateFilmGenres(@NonNull Film film) {
        List<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        List<Genre> list = genres.stream()
                .filter(distinctByKey(Genre::getId))
                .collect(Collectors.toList());

        checkGenres(list);
        dropGenresFor(film.getId());
        linkFilmWithGenres(film, list);
    }

    private void dropGenresFor(@NonNull Long filmId) {
        String query = "SELECT \"genre_id\" " +
                "FROM \"filmorate\".\"film_genre\" " +
                "WHERE \"film_id\" = ?";
        List<Long> current = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong(1), filmId);
        current.forEach(item ->
                jdbcTemplate.update("DELETE FROM \"filmorate\".\"film_genre\" WHERE \"film_id\"=? AND \"genre_id\"=?", filmId, item)
        );
    }

    private List<Genre> getGenresFor(@NonNull Film item) {
        return jdbcTemplate.query(
                "SELECT g.\"genre_id\" , g.\"genre_name\" " +
                        "FROM \"filmorate\".\"film_genre\" fg " +
                        "LEFT JOIN \"filmorate\".\"genre\" g ON g.\"genre_id\" = FG .\"genre_id\" " +
                        "WHERE FG .\"film_id\" = ?",
                GENRE_MAPPER,
                item.getId()
        );
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}