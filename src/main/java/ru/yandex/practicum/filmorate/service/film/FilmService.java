package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    void addLike(int filmId, Long userId);

    void removeLike(int filmId, Long userId);

    Collection<Film> getPopularFilms(Integer count);
}
