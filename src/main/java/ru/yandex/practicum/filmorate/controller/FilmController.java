package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.common.FilmSearchType;
import ru.yandex.practicum.filmorate.common.FilmSortType;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Film findById(@PathVariable int id) {
        return filmService.findById(id);
    }

    @GetMapping("/search")
    public List<Film> search(@RequestParam(name = "query") String query,
                             @RequestParam(name = "by") List<FilmSearchType> listBy) {
        return filmService.search(query, listBy);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count,
                                     @RequestParam(required = false) Integer genreId,
                                     @RequestParam(required = false) Integer year) {
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping(value = "/common")
    public List<Film> getCommon(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommon(userId, friendId);
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> getByDirector(@PathVariable int directorId,
                                    @RequestParam(name = "sortBy") FilmSortType sortBy) {
        return filmService.getByDirector(directorId, sortBy);
    }

    @DeleteMapping(value = "/{filmId}")
    public void deleteFilmById(@PathVariable int filmId) {
        filmService.deleteFilmById(filmId);
    }
}
