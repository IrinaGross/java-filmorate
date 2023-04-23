package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DatabaseFilmLikeStorageTest {
    private final DatabaseFilmLikeStorage storage;

    @Test
    public void getRatingShouldExecuteCorrect() {
        Assertions.assertDoesNotThrow(storage::getRating);
    }
}