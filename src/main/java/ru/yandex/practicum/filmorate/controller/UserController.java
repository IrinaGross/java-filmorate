package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.controller.validation.FilmorateValidator.validate;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService service;

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> allUsers = service.getAllUsers();
        log.trace("На данный момент пользователей: {}", allUsers.size());
        return allUsers;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long userId) {
        return service.getUser(userId);
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        validate(user, false);
        service.addUser(user);
        log.trace("Пользователь сохранен");
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody @Valid User user) {
        validate(user, true);
        service.updateUser(user);
        log.trace("Пользователь сохранен");
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") @NonNull Long userId,
            @PathVariable("friendId") @NonNull Long friendId
    ) {
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") @NonNull Long userId,
            @PathVariable("friendId") @NonNull Long friendId
    ) {
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsFor(@PathVariable("id") @NonNull Long userId) {
        return service.getFriendsFor(userId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getSharedFriendsFor(
            @PathVariable("id") @NonNull Long userId,
            @PathVariable("otherId") @NonNull Long otherUserId
    ) {
        return service.getSharedFriendsFor(userId, otherUserId);
    }
}
