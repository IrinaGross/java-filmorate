package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film {
    private Long id;
    @NonNull
    @NotEmpty
    private final String name;
    @NonNull
    @NotEmpty
    @Length(max = 200)
    private final String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    @Min(1)
    private final Long duration;
}