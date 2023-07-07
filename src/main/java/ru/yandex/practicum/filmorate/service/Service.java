package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.List;

public interface Service<T extends Model> {
    List<T> findAll();

    T findById(int id);

    T create(T model);

    T update(T model);
}
