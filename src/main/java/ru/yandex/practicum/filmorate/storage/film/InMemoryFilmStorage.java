package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate filmDay = LocalDate.of(1895, 12, 28);
    private static final int descriptionLength = 200;
    private final Map<Integer, Film> films = new HashMap<>();
    private int nexID = 1;

    public Film addFilm(Film film) {
        if (getFilms().stream().map(Film::getName).equals(film.getName())) {
            log.error("Фильм {} ранее был добавлен", film);
            throw new FilmAlreadyExistException(String.format("Фильм %s ранее был добавлен", film));
        }
        checkFilm(film);
        film.setId(nexID);
        log.info("Присвоен ID фильму {}", nexID);
        nexID++;
        log.info("Обновлён nextID {}", nexID);
        films.put(film.getId(), film);
        log.info("Добавлено фильм {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            checkFilm(film);
            films.put(film.getId(), film);
            log.info("Обновлены данные фильма {}", film);
        } else {
            log.error("Фильм {} не может быть обновлён, т.к. не найден. Сначала добавьте фильм", film);
            throw new FilmNotFoundException("Фильм " + film + " не может быть обновлён, т.к. не найден. Сначала добавьте фильм");
        }
        return film;
    }

    public Collection<Film> getFilms() {
        log.info("Получен запрос getFilms. Возвращается {} записей", films.size());
        return new ArrayList<>(films.values());
    }

    public Film getFilm(int filmId) {
        if (films.containsKey(filmId)) {
            log.info("Получен запрос getFilm({}), Вернулся {}", filmId, films.get(filmId));
            return films.get(filmId);
        } else {
            log.error("Фильм c id {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", filmId));
        }
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
