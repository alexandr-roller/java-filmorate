package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage,
                       @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, Long userId) {
        userStorage.getUser(userId);
        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, Long userId) {
        userStorage.getUser(userId);
        filmStorage.getFilm(filmId).getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Получен запрос getPopularFilms({})", count);
        if (count == null || count <= 0) {
            count = 10;
        }
        return filmStorage.getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(f -> f.getLikes().size())))
                .limit(count).collect(Collectors.toList());
    }
}
