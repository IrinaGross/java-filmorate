package ru.yandex.practicum.filmorate.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.storage.Mappers.USER_MAPPER;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @NonNull
    public List<User> getAllUsers() {
        val query = "SELECT \"user_id\", \"user_name\", \"user_email\", \"user_login\", \"user_birthday\" " +
                "FROM \"filmorate\".\"user\"";
        return jdbcTemplate.query(query, USER_MAPPER);
    }

    @Override
    @NonNull
    public User getUser(@NonNull Long userId) {
        val query = "SELECT \"user_id\", \"user_name\", \"user_email\", \"user_login\", \"user_birthday\" " +
                "FROM \"filmorate\".\"user\" " +
                "WHERE \"user_id\" = ?";

        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(query, USER_MAPPER, userId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Пользователь с идентификатором %1$d не найден", userId), e);
        }
    }

    @Override
    public void addUser(@NonNull User user) {
        String query = "INSERT INTO \"filmorate\".\"user\" (\"user_name\", \"user_email\", \"user_login\", \"user_birthday\") " +
                "VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query, new String[]{"user_id"});
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setObject(4, user.getBirthday());
            return statement;
        }, keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public void updateUser(@NonNull User user) {
        checkUserId(user.getId());

        updateUserInternal(user);
    }

    private void checkUserId(@Nullable Long userId) {
        if (userId == null) {
            throw new NotFoundException(String.format("Пользователь с идентификатором %1$d не найден", userId));
        }
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) " +
                    "FROM \"filmorate\".\"user\" " +
                    "WHERE \"user_id\" = ?", Integer.class, userId);
            if (count == null || count == 0) {
                throw new NotFoundException(String.format("Пользователь с идентификатором %1$d не найден", userId));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Пользователь с идентификатором %1$d не найден", userId), e);
        }
    }

    private void updateUserInternal(@NonNull User user) {
        String query = "UPDATE \"filmorate\".\"user\" " +
                "SET \"user_name\"= ?, \"user_email\"= ?, \"user_login\"= ?, \"user_birthday\"= ? " +
                "WHERE \"user_id\" = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setObject(4, user.getBirthday());
            statement.setLong(5, user.getId());
            return statement;
        });
    }
}
