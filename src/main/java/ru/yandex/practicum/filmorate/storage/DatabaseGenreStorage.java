package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.storage.Mappers.GENRE_MAPPER;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @NonNull
    public List<Genre> findAll() {
        String query = "SELECT \"genre_id\", \"genre_name\" " +
                "FROM \"filmorate\".\"genre\"";
        return jdbcTemplate.query(query, GENRE_MAPPER);
    }

    @Override
    @NonNull
    public Genre getById(@NonNull Long genreId) {
        String query = "SELECT \"genre_id\", \"genre_name\" " +
                "FROM \"filmorate\".\"genre\" " +
                "WHERE \"genre_id\" = ?";
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(query, GENRE_MAPPER, genreId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Жанр с идентификатором %1$d не найден", genreId), e);
        }
    }
}
