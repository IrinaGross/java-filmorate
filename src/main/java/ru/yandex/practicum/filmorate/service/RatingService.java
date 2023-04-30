package ru.yandex.practicum.filmorate.service;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface RatingService {
    @NonNull
    List<MpaRating> getAll();

    @NonNull
    MpaRating getById(Long ratingId);
}
