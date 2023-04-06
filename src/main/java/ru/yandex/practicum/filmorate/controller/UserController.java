package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.checkExist;
import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SuppressWarnings("unused")
@Slf4j
@RestController
public class UserController {

    private long id;
    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.trace("На данный момент пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        validate(user, false);
        long id = ++this.id;
        user.setId(id);
        users.put(id, user);
        log.trace("Пользователь сохранен");
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody @Valid User user) {
        validate(user, true);
        checkExist(users, user);
        users.put(user.getId(), user);
        log.trace("Пользователь сохранен");
        return user;
    }
}
