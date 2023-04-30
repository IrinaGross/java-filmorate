package ru.yandex.practicum.filmorate.controller.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmControllerTest {
    private static Validator validator;
    private static final MpaRating RATING = MpaRating.builder().id(0L).name("G").build();

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validateFilmWithoutIdDoesNotThrow() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(0, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    public void validateFilmWithEmptyNameShouldContainsError() {
        Film film = Film.builder()
                .name("")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    public void validateFilmWithNullNameShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () -> Film.builder()
                .name(null)
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build()
        );
    }

    @Test
    public void validateFilmWithMaxDescriptionShouldContainsError() {
        String description = "a".repeat(201);
        Film film = Film.builder()
                .name("Человек-паук")
                .description(description)
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    public void validateFilmWithNullDescriptionShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () -> Film.builder()
                .name("Человек-паук")
                .description(null)
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build()
        );
    }

    @Test
    public void validateFilmWithBeforeReleaseDateShouldThrowError() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(1800, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validate(film, false));
    }

    @Test
    public void validateFilmWithAfterReleaseDateShouldThrowError() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2800, 5, 2))
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validate(film, false));
    }

    @Test
    public void validateFilmWithNullReleaseDateShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () -> Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(null)
                .duration(8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build()
        );
    }

    @Test
    public void validateFilmWithZeroDurationShouldContainsError() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(0L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }

    @Test
    public void validateFilmWithNullDurationShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () -> Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(null)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build()
        );
    }

    @Test
    public void validateFilmWithNegativeDurationShouldContainsError() {
        Film film = Film.builder()
                .name("Человек-паук")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2002, 5, 2))
                .duration(-8880L)
                .mpa(RATING)
                .genres(Collections.emptyList())
                .build();
        Set<ConstraintViolation<Film>> set = validator.validate(film);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(film, false));
    }
}
