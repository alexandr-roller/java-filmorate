package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                          @Qualifier("FilmServiceDao") FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable("id") Integer filmId) {
        filmStorage.removeFilm(filmId);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        return filmStorage.getFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam(name = "userId") Long userId,
                                           @RequestParam(name = "friendId") Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/{search}")
    public Collection<Film> search(@RequestParam(name = "query") String query,
                                   @RequestParam(name = "by") List<String> by) {
        return filmService.search(query, by)
    }
}
