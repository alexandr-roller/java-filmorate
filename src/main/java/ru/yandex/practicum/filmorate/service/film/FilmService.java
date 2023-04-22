package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
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

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, Long userId) {
        if (filmStorage.getFilms().stream().map(Film::getId)
                .collect(Collectors.toList()).contains(filmId)
                && userStorage.getUsers().stream().map(User::getId)
                .collect(Collectors.toList()).contains(userId)) {
            filmStorage.getFilm(filmId).getLikes().add(userId);
        } else {
            log.error("Ошибка с полем filmId или userId");
            throw new IncorrectParameterException("filmId или userId");
        }
    }

    public void removeLike(int filmId, Long userId) {
        if (filmStorage.getFilms().stream().map(Film::getId)
                .collect(Collectors.toList()).contains(filmId)
                && userStorage.getUsers().stream().map(User::getId)
                .collect(Collectors.toList()).contains(userId)) {
            filmStorage.getFilm(filmId).getLikes().remove(userId);
        } else {
            log.error("Ошибка с полем filmId или userId");
            throw new IncorrectParameterException("filmId или userId");
        }
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
