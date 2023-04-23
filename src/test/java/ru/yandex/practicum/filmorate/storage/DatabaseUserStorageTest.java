package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DatabaseUserStorageTest {
    private final DatabaseUserStorage storage;

    @Test
    void getAllShouldExecuteCorrect() {
        Assertions.assertDoesNotThrow(storage::getAllUsers);
    }

    @Test
    void getByIdShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> storage.getUser(-1L));
    }

    @Test
    public void addUserShouldExecuteCorrect() {
        int size = storage.getAllUsers().size();
        User user = User.builder()
                .birthday(LocalDate.of(1994, 12, 28))
                .name("E")
                .login("dolore")
                .email("email@t.k")
                .build();

        Assertions.assertDoesNotThrow(() -> storage.addUser(user));
        Assertions.assertEquals(size + 1, storage.getAllUsers().size());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void updateUserWithNullIdShouldThrowNotFoundException() {
        User user = User.builder()
                .birthday(LocalDate.of(1994, 12, 28))
                .name("E")
                .login("dolore")
                .email("email@t.k")
                .build();

        Assertions.assertThrows(NotFoundException.class, () -> storage.updateUser(user));
    }

    @Test
    public void updateUserWithNegativeIdShouldThrowNotFoundException() {
        User user = User.builder()
                .id(-1L)
                .birthday(LocalDate.of(1994, 12, 28))
                .name("E")
                .login("dolore")
                .email("email@t.k")
                .build();

        Assertions.assertThrows(NotFoundException.class, () -> storage.updateUser(user));
    }

    @Test
    public void updateUserShouldExecuteCorrect() {
        int size = storage.getAllUsers().size();
        storage.addUser(User.builder()
                .birthday(LocalDate.of(1994, 12, 28))
                .name("E")
                .login("dolore")
                .email("email@t.k")
                .build());

        String newName = "Dolore";

        User databaseUser = storage.getAllUsers().get(0);
        storage.updateUser(databaseUser
                .toBuilder()
                .name(newName)
                .build());

        User user = storage.getUser(databaseUser.getId());
        Assertions.assertEquals(newName, user.getName());
        Assertions.assertEquals(size + 1, storage.getAllUsers().size());
    }
}