package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.FilmSearchType;
import ru.yandex.practicum.filmorate.common.FilmSortType;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmServiceImpl extends AbstractService<Film> implements FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventService eventService;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage storage, UserStorage userStorage, EventService eventService, DirectorStorage directorStorage) {
        super(storage);
        this.userStorage = userStorage;
        this.filmStorage = storage;
        this.eventService = eventService;
        this.directorStorage = directorStorage;
    }

    @Override
    protected String getModelName() {
        return "Film";
    }

    @Override
    public Film create(Film film) {
        Film createdFilm = super.create(film);
        log.info("Film is created");
        return createdFilm;
    }

    @Override
    public Film update(Film film) throws FilmNotFoundException {
        try {
            Film updatedFilm = super.update(film);
            log.info("Film is updated");
            return updatedFilm;
        } catch (ModelNotFoundException ex) {
            throw new FilmNotFoundException(film.getId());
        }
    }

    @Override
    public void addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = findById(filmId);
        User user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.addLike(film, user);
        log.info("Like is added");

        eventService.create(
                Event.builder()
                        .userId(userId)
                        .type(Event.EventType.LIKE)
                        .operation(Event.OperationType.ADD)
                        .entityId(filmId)
                        .build()
        );
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = findById(filmId);
        User user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.deleteLike(film, user);
        log.info("Like is deleted");

        eventService.create(
                Event.builder()
                        .userId(userId)
                        .type(Event.EventType.LIKE)
                        .operation(Event.OperationType.REMOVE)
                        .entityId(filmId)
                        .build()
        );
    }

    @Override
    public List<Film> getPopular(int count, Integer genreId, Integer year) {
        return filmStorage.findPopular(count, genreId, year);
    }

    @Override
    public List<Film> getCommon(int userId, int friendId) {
        userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        return filmStorage.getCommon(userId, friendId);
    }

    @Override
    public List<Film> getByDirector(int directorId, FilmSortType sortBy) {
        directorStorage.findById(directorId).orElseThrow(() -> new DirectorNotFoundException(directorId));
        return filmStorage.getByDirector(directorId, sortBy);
    }

    @Override
    public List<Film> search(String query, List<FilmSearchType> listBy) {
        return filmStorage.search(query, listBy);
    }

    @Override
    public void deleteFilmById(int filmId) {
        filmStorage.deleteFilmById(filmId);
        log.info("Film is deleted");
    }
}
