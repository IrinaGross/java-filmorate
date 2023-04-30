package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
class RatingServiceImpl implements RatingService {
    @Qualifier("db")
    private final RatingStorage storage;

    @Override
    @NonNull
    public List<MpaRating> getAll() {
        return storage.getAll();
    }

    @Override
    @NonNull
    public MpaRating getById(@NonNull Long ratingId) {
        return storage.getById(ratingId);
    }
}
