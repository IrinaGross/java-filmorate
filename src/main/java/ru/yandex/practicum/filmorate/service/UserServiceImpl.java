package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserFriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    @Qualifier("db")
    private final UserStorage userStorage;
    @Qualifier("db")
    private final UserFriendsStorage userFriendsStorage;

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public void addUser(User user) {
        userStorage.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        userFriendsStorage.addFriend(user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        userFriendsStorage.deleteFriend(user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriendsFor(Long userId) {
        User user = userStorage.getUser(userId);
        return userFriendsStorage.getFriendsFor(user.getId())
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getSharedFriendsFor(Long userId, Long otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        return userFriendsStorage.getSharedFriendsFor(user.getId(), otherUser.getId())
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
