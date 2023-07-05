package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.common.FilmSearchType;
import ru.yandex.practicum.filmorate.common.FilmSortType;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> findPopular(int count, Integer genreId, Integer year);

    List<Film> search(String query, List<FilmSearchType> listBy);

    List<Film> getCommon(int userId, int friendId);

    List<Film> getByDirector(int directorId, FilmSortType sortBy);

    void deleteFilmById(int filmId);
}
