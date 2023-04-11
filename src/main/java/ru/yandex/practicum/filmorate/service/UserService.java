package ru.yandex.practicum.filmorate.service;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    @NonNull
    List<User> getAllUsers();

    @NonNull
    User getUser(@NonNull Long userId);

    void addUser(@NonNull User user);

    void updateUser(@NonNull User user);

    void addFriend(@NonNull Long userId, @NonNull Long friendId);

    void deleteFriend(@NonNull Long userId, @NonNull Long friendId);

    @NonNull
    List<User> getFriendsFor(@NonNull Long userId);

    @NonNull
    List<User> getSharedFriendsFor(@NonNull Long userId, @NonNull Long otherUserId);
}
