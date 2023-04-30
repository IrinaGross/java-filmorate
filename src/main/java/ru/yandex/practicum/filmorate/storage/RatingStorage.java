package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface RatingStorage {
    @NonNull
    List<MpaRating> getAll();

    @NonNull
    MpaRating getById(@NonNull Long ratingId);
}
