package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    @Test
    public void validateUserWithoutIdDoesNotThrow() {
        User user = new User("email@t.k", "p", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertDoesNotThrow(() -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithEmptyEmailShouldThrowError() {
        User user = new User("", "p", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithNullEmailShouldThrowError() {
        User user = new User(null, "p", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithNotAtEmailShouldThrowError() {
        User user = new User("fkjv", "p", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithEmptyLoginShouldThrowError() {
        User user = new User("email@t.k", "", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithNullLoginShouldThrowError() {
        User user = new User("email@t.k", null, "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithBlankLoginShouldThrowError() {
        User user = new User("email@t.k", " ", "E", LocalDate.of(1994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithNullNameShouldDoesThrow() {
        User user = new User("email@t.k", "p", null, LocalDate.of(1994, 12, 28));
        Assertions.assertDoesNotThrow(() -> UserController.validate(user, false));
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void validateUserWithNullBirthdayShouldThrowError() {
        User user = new User("email@t.k", " ", "E", null);
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }

    @Test
    public void validateUserWithBirthdayAfterShouldThrowError() {
        User user = new User("email@t.k", " ", "E", LocalDate.of(2994, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user, false));
    }
}