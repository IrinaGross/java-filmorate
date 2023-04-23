package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatus;

import java.sql.ResultSet;
import java.util.stream.Stream;

@Component
@Qualifier("db")
@RequiredArgsConstructor
class DatabaseUserFriendsStorage implements UserFriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(@NonNull Long userId, @NonNull Long friendId) {
        FriendStatus friendStatus = getFriendStatus(userId, friendId);
        String query = "INSERT INTO \"filmorate\".\"friends\" (\"user_id\", \"followed_user_id\", \"friend_status\") " +
                "VALUES(?, ?, ?)";
        jdbcTemplate.update(query, userId, friendId, friendStatus.name());
    }

    @Override
    public void deleteFriend(@NonNull Long userId, @NonNull Long friendId) {
        String query = "DELETE FROM \"filmorate\".\"friends\" " +
                "WHERE \"user_id\" = ? AND \"followed_user_id\" = ?";
        jdbcTemplate.update(query, userId, friendId);
    }

    @Override
    @NonNull
    public Stream<Long> getFriendsFor(@NonNull Long userId) {
        String query = "SELECT \"followed_user_id\" " +
                "FROM \"filmorate\".\"friends\" " +
                "WHERE \"user_id\" = ?";
        return jdbcTemplate.queryForStream(query, (ResultSet resultSet, int rowNum) -> resultSet.getLong(1), userId);
    }

    @Override
    public Stream<Long> getSharedFriendsFor(Long userId, Long otherUserId) {
        String query = "SELECT \"followed_user_id\" " +
                "FROM \"filmorate\".\"friends\" " +
                "WHERE \"followed_user_id\" IN (" +
                "SELECT \"followed_user_id\" " +
                "FROM \"filmorate\".\"friends\" " +
                "WHERE \"user_id\" = ?" +
                ")" +
                "AND \"user_id\" = ?";
        return jdbcTemplate.queryForStream(query, (ResultSet resultSet, int rowNum) -> resultSet.getLong(1), userId, otherUserId);
    }

    @NonNull
    private FriendStatus getFriendStatus(@NonNull Long userId, @NonNull Long friendId) {
        String query = "SELECT COUNT(*) " +
                "FROM \"filmorate\".\"friends\" f " +
                "WHERE f.\"user_id\" = ? AND f.\"followed_user_id\" = ? AND f.\"friend_status\" = 'NOT_APPROVED'";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, friendId, userId);
        if (count == null || count == 0) {
            return FriendStatus.NOT_APPROVED;
        } else {
            return FriendStatus.APPROVED;
        }
    }
}
