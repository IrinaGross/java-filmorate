package ru.yandex.practicum.filmorate.controller.validator;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SpringBootTest
public class FilmControllerTest {
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validateFilmWithoutIdDoesNotThrow() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), 8880L);
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(0, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    public void validateFilmWithEmptyNameShouldContainsError() {
        Film film = new Film("", "Про человека паука", LocalDate.of(2002, 5, 2), 8880L);
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateFilmWithNullNameShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new Film(null, "Про человека паука", LocalDate.of(2002, 5, 2), 8880L));
    }

    @Test
    public void validateFilmWithMaxDescriptionShouldContainsError() {
        String description = "a".repeat(201);
        Film film = new Film("Человек-паук", description, LocalDate.of(2002, 5, 2), 8880L);
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateFilmWithNullDescriptionShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new Film("Человек-паук", null, LocalDate.of(2002, 5, 2), 8880L));
    }

    @Test
    public void validateFilmWithBeforeReleaseDateShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(1800, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> validate(film, false));
    }

    @Test
    public void validateFilmWithAfterReleaseDateShouldThrowError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2800, 5, 2), 8880L);
        Assertions.assertThrows(ValidationException.class, () -> validate(film, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateFilmWithNullReleaseDateShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new Film("Человек-паук", "Про человека паука", null, 8880L));
    }

    @Test
    public void validateFilmWithZeroDurationShouldContainsError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), 0L);
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateFilmWithNullDurationShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), null));
    }

    @Test
    public void validateFilmWithNegativeDurationShouldContainsError() {
        Film film = new Film("Человек-паук", "Про человека паука", LocalDate.of(2002, 5, 2), -8800L);
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }
}
