package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilmConverter {

    public static Film fromResultSet(ResultSet rs) throws SQLException {
        return Film.filmBuilder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(MpaConverter.fromResultSet(rs))
                .build();
    }

    public static List<Film> listFromResultSet(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        Film currentFilm = null;

        while (rs.next()) {
            if (currentFilm == null || currentFilm.getId() != rs.getInt("film_id")) {
                currentFilm = fromResultSet(rs);
                films.add(currentFilm);
            }
            if (rs.getInt("genre_id") != 0) {
                currentFilm.getGenres().add(GenreConverter.fromResultSet(rs));
            }
            if (rs.getInt("director_id") != 0) {
                currentFilm.getDirectors().add(DirectorConverter.fromResultSet(rs));
            }
        }

        return films;
    }
}
