package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

@Data
@Builder
@RequiredArgsConstructor
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Genre {
    @NotNull
    private final Long id;
    @Nullable
    private String name;
}
