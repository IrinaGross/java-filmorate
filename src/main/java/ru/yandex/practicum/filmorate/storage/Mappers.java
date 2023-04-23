package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Mappers {

    static final RowMapper<Genre> GENRE_MAPPER = (rs, rowNum) -> Genre.builder()
            .id(rs.getLong("genre_id"))
            .name(rs.getString("genre_name"))
            .build();

    static final RowMapper<Film> FILM_MAPPER = (rs, rowNum) -> {
        MpaRating mpaRating = null;
        Long mpaId = rs.getLong("mpa_id");
        if (mpaId > 0) {
            String mpaName = rs.getString("mpa_name");
            mpaRating = MpaRating.builder()
                    .id(mpaId)
                    .name(mpaName)
                    .build();
        }

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_desc"))
                .duration(rs.getLong("film_duration"))
                .releaseDate(rs.getObject("film_release_date", LocalDate.class))
                .mpa(mpaRating)
                .build();
    };

    static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> User.builder()
            .id(rs.getLong("user_id"))
            .name(rs.getString("user_name"))
            .email(rs.getString("user_email"))
            .login(rs.getString("user_login"))
            .birthday(rs.getObject("user_birthday", LocalDate.class))
            .build();

    static final RowMapper<MpaRating> RATING_MAPPER = (rs, rowNum) -> MpaRating.builder()
            .id(rs.getLong("mpa_id"))
            .name(rs.getString("mpa_name"))
            .build();
}
