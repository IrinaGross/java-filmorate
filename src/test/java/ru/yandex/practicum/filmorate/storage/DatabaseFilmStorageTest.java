package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DatabaseFilmStorageTest {
    private final DatabaseFilmStorage storage;

    @Test
    public void getAllFilmsShouldExecuteCorrect() {
        Assertions.assertDoesNotThrow(storage::getAllFilms);
    }

    @Test
    public void getFilmByIdWithNegativeIdShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> storage.getFilm(-1L));
    }

    @Test
    public void addFilmWithNegativeIdsShouldThrowNotFoundException() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(MpaRating.builder().id(1L).name("G").build())
                .genres(Collections.singletonList(Genre.builder().id(-1L).build()))
                .build();

        Assertions.assertThrows(NotFoundException.class, () -> storage.addFilm(film));
    }

    @Test
    public void updateFilmWithNegativeIdsShouldThrowNotFoundException() {
        Film film = Film.builder()
                .id(-1L)
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(MpaRating.builder().id(1L).name("G").build())
                .genres(Collections.singletonList(Genre.builder().id(-1L).build()))
                .build();

        Assertions.assertThrows(NotFoundException.class, () -> storage.updateFilm(film));
    }
}