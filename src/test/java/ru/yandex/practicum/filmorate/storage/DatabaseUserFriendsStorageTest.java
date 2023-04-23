package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseUserFriendsStorageTest {
    private final DatabaseUserStorage userStorage;
    private final DatabaseUserFriendsStorage storage;

    @Test
    @Order(1)
    public void addFriendShouldExecuteCorrectly() {
        userStorage.addUser(User.builder()
                .name("Коля")
                .email("kolya@rf.sa")
                .birthday(LocalDate.of(1998, 2, 1))
                .login("kolya")
                .build());
        userStorage.addUser(User.builder()
                .name("Максим")
                .email("maksim@rf.sa")
                .birthday(LocalDate.of(1998, 2, 1))
                .login("maksim")
                .build());

        List<User> allUsers = userStorage.getAllUsers();
        User k = allUsers.get(0);
        User m = allUsers.get(1);

        Assertions.assertDoesNotThrow(() -> storage.addFriend(k.getId(), m.getId()));
    }

    @Test
    @Order(2)
    public void getFriendsForShouldExecuteCorrectly() {
        User k = userStorage.getAllUsers().get(0);
        Set<Long> kFriends = storage.getFriendsFor(k.getId()).collect(Collectors.toSet());
        Assertions.assertEquals(1, kFriends.size());
    }

    @Test
    @Order(3)
    public void deleteFriendShouldExecuteCorrectly() {
        List<User> allUsers = userStorage.getAllUsers();
        User k = allUsers.get(0);
        User m = allUsers.get(1);

        Assertions.assertDoesNotThrow(() -> storage.deleteFriend(k.getId(), m.getId()));
    }

    @Test
    @Order(4)
    public void getFriendsForShouldBeEmpty() {
        User k = userStorage.getAllUsers().get(0);
        Set<Long> kFriends = storage.getFriendsFor(k.getId()).collect(Collectors.toSet());
        Assertions.assertEquals(0, kFriends.size());
    }

    @Test
    @Order(5)
    public void getSharedFriendsShouldExecuteCorrectly() {
        List<User> allUsers = userStorage.getAllUsers();
        User k = allUsers.get(0);
        User m = allUsers.get(1);

        userStorage.addUser(User.builder()
                .name("Яков")
                .email("jakov@rf.sa")
                .birthday(LocalDate.of(2010, 2, 1))
                .login("jakov")
                .build());

        User ja = userStorage.getAllUsers().get(2);
        storage.addFriend(k.getId(), ja.getId());
        storage.addFriend(m.getId(), ja.getId());

        Assertions.assertDoesNotThrow(() -> {
            List<Long> friends = storage.getSharedFriendsFor(k.getId(), m.getId()).collect(Collectors.toList());
            Assertions.assertEquals(1, friends.size());
            Assertions.assertEquals(ja.getId(), friends.get(0));
        });
    }
}