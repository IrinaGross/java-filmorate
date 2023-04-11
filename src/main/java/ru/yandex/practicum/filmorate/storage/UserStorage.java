package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    @NonNull
    List<User> getAllUsers();

    @NonNull
    User getUser(@NonNull Long userId);

    void addUser(@NonNull User user);

    void updateUser(@NonNull User user);
}
