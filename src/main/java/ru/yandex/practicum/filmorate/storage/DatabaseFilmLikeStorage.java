package ru.yandex.practicum.filmorate.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static ru.yandex.practicum.filmorate.utils.MapUtils.getOrEmptySet;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseFilmLikeStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void likeFilm(@NonNull Long filmId, @NonNull Long userId) {
        String query = "INSERT INTO \"filmorate\".\"user_film\" (\"user_id\", \"film_id\") " +
                "VALUES(?, ?);\n";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1, userId);
            statement.setLong(2, filmId);
            return statement;
        });
    }

    @Override
    public void unlikeFilm(@NonNull Long filmId, @NonNull Long userId) {
        String query = "DELETE FROM \"filmorate\".\"user_film\" " +
                "WHERE \"user_id\" = ? AND \"film_id\" = ?";

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1, userId);
            statement.setLong(2, filmId);
            return statement;
        });
    }

    @Override
    public Map<Long, Set<Long>> getRating() {
        String query = "SELECT \"user_id\", \"film_id\" FROM \"filmorate\".\"user_film\";\n";
        return Objects.requireNonNull(jdbcTemplate.query(query, (ResultSetExtractor<Map<Long, Set<Long>>>) rs -> {
            HashMap<Long, Set<Long>> mapRet = new HashMap<>();
            while (rs.next()) {
                long key = rs.getLong("film_id");
                Set<Long> set = getOrEmptySet(mapRet, key);
                set.add(rs.getLong("user_id"));
                mapRet.put(key, set);
            }
            return mapRet;
        }));
    }
}
