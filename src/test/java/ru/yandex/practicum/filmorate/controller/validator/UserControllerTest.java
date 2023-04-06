package ru.yandex.practicum.filmorate.controller.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SpringBootTest
class UserControllerTest {
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validateUserWithoutIdDoesNotThrow() {
        User user = new User("email@t.k", "p", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertDoesNotThrow(() -> validate(user, false));
    }

    @Test
    public void validateUserWithEmptyEmailShouldContainsError() {
        User user = new User("", "p", "E", LocalDate.of(1994, 12, 28));
        Set<ConstraintViolation<User>> set = validator.validate(user);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(user, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateUserWithNullEmailShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new User(null, "p", "E", LocalDate.of(1994, 12, 28)));
    }

    @Test
    public void validateUserWithNotAtEmailShouldContainsError() {
        User user = new User("fkjv", "p", "E", LocalDate.of(1994, 12, 28));
        Set<ConstraintViolation<User>> set = validator.validate(user);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(user, false));
    }

    @Test
    public void validateUserWithEmptyLoginShouldContainsError() {
        User user = new User("email@t.k", "", "E", LocalDate.of(1994, 12, 28));
        Set<ConstraintViolation<User>> set = validator.validate(user);
        Assertions.assertEquals(2, set.size());
        Assertions.assertDoesNotThrow(() -> validate(user, false));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateUserWithNullLoginShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new User("email@t.k", null, "E", LocalDate.of(1994, 12, 28)));
    }

    @Test
    public void validateUserWithBlankLoginShouldContainsError() {
        User user = new User("email@t.k", " ", "E", LocalDate.of(1994, 12, 28));
        Set<ConstraintViolation<User>> set = validator.validate(user);
        Assertions.assertEquals(1, set.size());
        Assertions.assertDoesNotThrow(() -> validate(user, false));
    }

    @Test
    public void validateUserWithNullNameShouldDoesThrow() {
        User user = new User("email@t.k", "p", null, LocalDate.of(1994, 12, 28));
        Set<ConstraintViolation<User>> set = validator.validate(user);
        Assertions.assertEquals(0, set.size());
        Assertions.assertDoesNotThrow(() -> validate(user, false));
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void validateUserWithNullBirthdayShouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new User("email@t.k", " ", "E", null));
    }

    @Test
    public void validateUserWithBirthdayAfterShouldThrowError() {
        User user = new User("email@t.k", " ", "E", LocalDate.of(2994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> validate(user, false));
    }
}