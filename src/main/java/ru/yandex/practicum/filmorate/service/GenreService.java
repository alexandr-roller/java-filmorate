package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.impl.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findById(int id);
}
