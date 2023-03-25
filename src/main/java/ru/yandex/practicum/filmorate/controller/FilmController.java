package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate filmDay = LocalDate.of(1895, 12, 28);
    private static final int descriptionLength = 200;
    private final Map<Integer, Film> films = new HashMap<>();
    private int nexID = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        checkFilm(film);
        film.setId(nexID);
        log.info("Присвоен ID фильму {}", nexID);
        nexID++;
        log.info("Обновлён nextID {}", nexID);
        films.put(film.getId(), film);
        log.info("Добавлено фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            checkFilm(film);
            films.put(film.getId(), film);
            log.info("Обновлены данные фильма {}", film);
        } else {
            log.error("Фильм {} не может быть обновлён, т.к. не найден. Сначала добавьте фильм", film);
            throw new ValidationException("Фильм " + film + " не может быть обновлён, т.к. не найден. Сначала добавьте фильм");
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос getFilms. Возвращается {} записей", films.size());
        return new ArrayList<>(films.values());
    }

    private void checkFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
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
