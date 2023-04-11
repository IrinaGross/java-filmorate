package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class InMemoryUserFriendsStorage implements UserFriendsStorage {
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    @Override
    public void addFriend(Long userId, Long friendId) {
        Set<Long> userFriends = getOrDefault(userId);
        userFriends.add(friendId);
        friends.put(userId, userFriends);

        Set<Long> friendFriends = getOrDefault(friendId);
        friendFriends.add(userId);
        friends.put(friendId, friendFriends);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        Set<Long> userFriends = getOrDefault(userId);
        userFriends.remove(friendId);
        friends.put(userId, userFriends);

        Set<Long> friendFriends = getOrDefault(friendId);
        friendFriends.remove(userId);
        friends.put(friendId, friendFriends);
    }

    @Override
    public Stream<Long> getFriendsFor(Long userId) {
        return Collections.unmodifiableCollection(getOrDefault(userId)).stream();
    }

    @Override
    public Stream<Long> getSharedFriendsFor(Long userId, Long otherUserId) {
        Set<Long> userFriends = getOrDefault(userId);
        Set<Long> otherUserFriends = getOrDefault(otherUserId);
        return userFriends.stream()
                .filter(otherUserFriends::contains);
    }

    private Set<Long> getOrDefault(Long userId) {
        return friends.getOrDefault(userId, new HashSet<>());
    }
}
