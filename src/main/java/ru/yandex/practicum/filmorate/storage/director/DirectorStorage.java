package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {
    Director addDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(Integer directorId);

    Collection<Director> getDirectors();

    Director getDirector(Integer directorId);
}
