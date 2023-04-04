package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {
    @Test
    public void validateFilmWithoutIdDoesNotThrow() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), 8880L);
        Assertions.assertDoesNotThrow(() -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithEmptyNameShouldThrowError() {
        Film film = new Film("", "Про человека паука", LocalDate.of(2002, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNullNameShouldThrowError() {
        Film film = new Film(null, "Про человека паука", LocalDate.of(2002, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithMaxDescriptionShouldThrowError() {
        String description = "a".repeat(201);
        Film film = new Film("Человек-паук", description, LocalDate.of(2002, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNullDescriptionShouldThrowError() {
        Film film = new Film("Человек-паук", null, LocalDate.of(2002, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithBeforeReleaseDateShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(1800, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithAfterReleaseDateShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2800, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNullReleaseDateShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", null, 8880L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNulDurationShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), 0L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNullDurationShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), null);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }

    @Test
    public void validateFilmWithNegativeDurationShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), -8800L);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film, false));
    }
}
