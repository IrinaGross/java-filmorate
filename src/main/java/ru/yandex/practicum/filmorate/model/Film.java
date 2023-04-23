package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    @Nullable
    private final MpaRating mpa;
    @Nullable
    private List<Genre> genres;
}