package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class User {
    private Long id;
    @NonNull
    @NotEmpty
    @Email
    private final String email;
    @NonNull
    @NotEmpty
    @NotBlank
    private final String login;
    private final String name;
    @NonNull
    private final LocalDate birthday;

    public String getName() {
        if (Objects.isNull(name) || name.isEmpty()) {
            return login;
        } else {
            return name;
        }
    }
}