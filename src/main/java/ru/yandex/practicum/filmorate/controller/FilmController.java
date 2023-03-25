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
    private final LocalDate filmDay = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int nexID = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (checkFilm(film)) {
            film.setId(nexID);
            log.info("Присвоен ID фильму" + nexID);
            nexID++;
            log.info("Обновлён nextID");
            films.put(film.getId(), film);
            log.info("Добавлено фильм " + film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId()) && checkFilm(film)) {
            films.put(film.getId(), film);
            log.info("Обновлены данные фильма " + film);
        } else {
            log.error("Такая задача не найдена");
            throw new ValidationException();
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос getFilms");
        return new ArrayList<>(films.values());
    }

    private boolean checkFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Отсутствует название фильма");
            throw new ValidationException();
        } else if (film.getDescription().length() > 200) {
            log.error("Превышена длительность описания");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(filmDay)) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException();
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException();
        } else {
            return true;
        }
    }
}
