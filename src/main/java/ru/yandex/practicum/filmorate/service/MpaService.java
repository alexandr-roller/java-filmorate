package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.impl.Mpa;

import java.util.List;

public interface MpaService {
    List<Mpa> findAll();

    Mpa findById(int id);
}
