package ru.yandex.practicum.filmorate.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.storage.Mappers.RATING_MAPPER;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseRatingStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @NonNull
    public List<MpaRating> getAll() {
        String query = "SELECT * FROM \"filmorate\".\"mpa\"";
        return jdbcTemplate.query(query, RATING_MAPPER);
    }

    @Override
    @NonNull
    public MpaRating getById(@NonNull Long ratingId) {
        String query = "SELECT * FROM \"filmorate\".\"mpa\" WHERE \"mpa_id\" = ?";
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(query, RATING_MAPPER, ratingId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Запись рейтинга ассоциации кинокомпаний с идентификатором %1$d не найдена", ratingId), e);
        }
    }
}
