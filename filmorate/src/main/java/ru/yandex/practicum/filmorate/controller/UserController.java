package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Slf4j
@RestController
public class UserController {
    private final static String AT = "@";
    private final static LocalDate NOW_DATE = LocalDate.now();

    private long id;
    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.trace("На данный момент пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        validate(user, false);
        long id = ++this.id;
        user.setId(id);
        users.put(id, user);
        log.trace("Пользователь сохранен");
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        validate(user, true);
        if (!users.containsKey(user.getId())) {
            throw  new ValidationException("Такого пользователя нет");
        }
        users.put(user.getId(), user);
        log.trace("Пользователь сохранен");
        return user;
    }

    public static void validate(User user, Boolean checkId) {
        if ((checkId && Objects.isNull(user.getId()))
                || Objects.isNull(user.getEmail()) || user.getEmail().isEmpty() || !user.getEmail().contains(AT)
                || Objects.isNull(user.getLogin()) || user.getLogin().isEmpty() || user.getLogin().isBlank()
                || Objects.isNull(user.getName())
                || Objects.isNull(user.getBirthday()) || user.getBirthday().isAfter(NOW_DATE)) {
            log.warn("Пользователь не сохранен из-за некорректных данных");
            throw new ValidationException("Некорректные данные пользователя");
        }
    }

}
