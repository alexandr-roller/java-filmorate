package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate filmDay = LocalDate.of(1895, 12, 28);
    private static final int descriptionLength = 200;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Получен запрос addFilm({})", film);
        if (getFilms().stream().map(Film::getName).collect(Collectors.toSet()).contains(film.getName())) {
            log.error("Фильм {} ранее был добавлен", film.getName());
            throw new FilmAlreadyExistException(String.format("Фильм %s ранее был добавлен", film.getName()));
        }
        checkFilm(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        return getFilm(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос updateFilm({})", film);
        getFilm(film.getId());
        checkFilm(film);
        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?, RATE = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        if (film.getGenres() != null) {
            String deleteGenreSql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(deleteGenreSql, film.getId());
            String updateGenreSql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID)" +
                    "VALUES (?, ?)";
            film.getGenres().forEach(genre -> {
                jdbcTemplate.update(updateGenreSql, film.getId(), genre.getId());
                log.info("Обновлены данные в таблице жанров {}", genre);
            });
        }
        log.info("Обновлены данные фильма {}", film);
        return getFilm(film.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Получен запрос getFilms");
        String sql = "SELECT * FROM FILMS ";
        return jdbcTemplate.query(sql, this::mapRowtoFilm);
    }

    @Override
    public Film getFilm(int filmId) {
        try {
            log.info("Получен запрос getFilm({})", filmId);
            String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowtoFilm, filmId);
        } catch (Exception e) {
            log.error("Фильм c id {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", filmId));
        }
    }

    public Film mapRowtoFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(makeMpa(resultSet.getInt("MPA_ID")))
                .genres(makeGenres(resultSet.getInt("FILM_ID")))
                .rate(resultSet.getInt("RATE"))
                .build();
    }

    private Mpa makeMpa(int mpaId) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowtoMpa, mpaId);
    }

    private Set<Genre> makeGenres(int filmId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowGenre, filmId));
    }

    private Mpa mapRowtoMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }

    private Genre mapRowGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }

    private void checkFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.error("Отсутствует название фильма");
            throw new ValidationException("Отсутствует название фильма");
        } else if (film.getDescription().length() > descriptionLength) {
            log.error("Превышена длительность описания");
            throw new ValidationException("Длительность описания не должна превышать " + descriptionLength + " символов");
        } else if (film.getReleaseDate().isBefore(filmDay)) {
            log.error("Дата релиза — не раньше {}", filmDay);
            throw new ValidationException("Дата релиза — не раньше " + filmDay);
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}