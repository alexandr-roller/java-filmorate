package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.List;
import java.util.Optional;

public interface Storage<T extends Model> {
    List<T> findAll();

    Optional<T> findById(int id);

    T create(T model);

    T update(T model);
}
