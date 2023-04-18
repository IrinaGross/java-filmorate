package ru.yandex.practicum.filmorate.controller.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class FilmorateValidator {
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final LocalDate NOW_DATE = LocalDate.now();

    public static void validate(Film film, Boolean checkId) {
        if (checkId && Objects.isNull(film.getId()) ||
                film.getReleaseDate().isBefore(RELEASE_DATE) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("Фильм не сохранен из-за некорректных данных");
            throw new ValidationException("Некорректные данные фильма");
        }
    }

    public static void validate(User user, Boolean checkId) {
        if ((checkId && Objects.isNull(user.getId()))
                || user.getBirthday().isAfter(NOW_DATE)) {
            log.warn("Пользователь не сохранен из-за некорректных данных");
            throw new ValidationException("Некорректные данные пользователя");
        }
    }
}
