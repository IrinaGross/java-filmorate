package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DatabaseRatingStorageTest {
    public final DatabaseRatingStorage storage;

    @Test
    void getAllShouldExecuteCorrect() {
        Assertions.assertDoesNotThrow(storage::getAll);
    }

    @Test
    void getByIdShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> storage.getById(-1L));
    }
}