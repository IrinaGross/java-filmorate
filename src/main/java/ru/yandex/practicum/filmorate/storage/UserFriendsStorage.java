package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;

import java.util.stream.Stream;

public interface UserFriendsStorage {

    void addFriend(@NonNull Long userId, @NonNull Long friendId);

    void deleteFriend(@NonNull Long userId, @NonNull Long friendId);

    @NonNull
    Stream<Long> getFriendsFor(@NonNull Long userId);

    @NonNull
    Stream<Long> getSharedFriendsFor(@NonNull Long userId, @NonNull Long otherUserId);
}
