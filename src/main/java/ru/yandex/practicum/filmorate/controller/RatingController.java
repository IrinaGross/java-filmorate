package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService service;

    @GetMapping("/mpa")
    public List<MpaRating> findAll() {
        return service.getAll();
    }

    @GetMapping("/mpa/{id}")
    public MpaRating getGenre(@PathVariable("id") @NonNull Long ratingId) {
        return service.getById(ratingId);
    }
}
