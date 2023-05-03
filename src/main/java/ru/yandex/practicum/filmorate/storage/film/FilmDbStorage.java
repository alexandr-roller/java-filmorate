package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private static final LocalDate filmDay = LocalDate.of(1895, 12, 28);
    private static final int descriptionLength = 200;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsValue(film)) {
            log.error("Фильм {} ранее был добавлен", film);
            throw new FilmAlreadyExistException(String.format("Фильм %s ранее был добавлен", film));
        }
        checkFilm(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        return getFilms(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Film getFilm(int filmID) {
        return null;
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
