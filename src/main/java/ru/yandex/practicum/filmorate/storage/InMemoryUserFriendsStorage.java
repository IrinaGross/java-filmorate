package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static ru.yandex.practicum.filmorate.utils.MapUtils.getOrEmptySet;

@Component
class InMemoryUserFriendsStorage implements UserFriendsStorage {
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    @Override
    public void addFriend(Long userId, Long friendId) {
        Set<Long> userFriends = getOrEmptySet(friends, userId);
        userFriends.add(friendId);
        friends.put(userId, userFriends);

        Set<Long> friendFriends = getOrEmptySet(friends, friendId);
        friendFriends.add(userId);
        friends.put(friendId, friendFriends);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        Set<Long> userFriends = getOrEmptySet(friends, userId);
        userFriends.remove(friendId);
        friends.put(userId, userFriends);

        Set<Long> friendFriends = getOrEmptySet(friends, friendId);
        friendFriends.remove(userId);
        friends.put(friendId, friendFriends);
    }

    @Override
    public Stream<Long> getFriendsFor(Long userId) {
        return Collections.unmodifiableCollection(getOrEmptySet(friends, userId)).stream();
    }

    @Override
    public Stream<Long> getSharedFriendsFor(Long userId, Long otherUserId) {
        Set<Long> userFriends = getOrEmptySet(friends, userId);
        Set<Long> otherUserFriends = getOrEmptySet(friends, otherUserId);
        return userFriends.stream()
                .filter(otherUserFriends::contains);
    }
}
