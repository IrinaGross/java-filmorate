package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class User {
    private Long id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;

    public String getName() {
        if (Objects.isNull(name) || name.isEmpty()) {
            return login;
        } else {
            return name;
        }
    }
}