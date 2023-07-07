package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.common.FilmSearchType;
import ru.yandex.practicum.filmorate.common.FilmSortType;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryAbstractStorage<Film> implements FilmStorage {
    private final Set<Pair<Integer, Integer>> likes = new HashSet<>();

    @Override
    public void addLike(Film film, User user) {
        likes.add(Pair.of(film.getId(), user.getId()));
    }

    @Override
    public void deleteLike(Film film, User user) {
        likes.remove(Pair.of(film.getId(), user.getId()));
    }

    @Override
    public List<Film> findPopular(int count, Integer genreId, Integer year) {
        return likes.stream()
                .map(Pair::getFirst)
                .sorted((n1, n2) -> n2 - n1)
                .limit(count)
                .map(id -> findById(id).orElseThrow(() -> new FilmNotFoundException(id)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> search(String query, List<FilmSearchType> listBy) {
        return null;
    }

    @Override
    public List<Film> getCommon(int userId, int friendId) {
        return null;
    }

    @Override
    public List<Film> getByDirector(int directorId, FilmSortType sortBy) {
        return null;
    }

    @Override
    public void deleteFilmById(int filmId) {
    }
}
