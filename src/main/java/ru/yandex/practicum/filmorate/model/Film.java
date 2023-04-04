package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film {
    private Long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;
}