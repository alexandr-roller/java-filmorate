package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.impl.Director;

public interface DirectorService extends Service<Director> {
    void removeDirector(Integer directorId);
}
