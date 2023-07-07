package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.service.Service;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractService<T extends Model> implements Service<T> {
    protected final Storage<T> storage;

    protected AbstractService(Storage<T> storage) {
        this.storage = storage;
    }

    public List<T> findAll() {
        return storage.findAll().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public T findById(int id) throws ModelNotFoundException {
        return storage.findById(id).orElseThrow(() -> new ModelNotFoundException(id, getModelName()));
    }

    @Override
    public T create(T model) {
        return storage.create(model);
    }

    @Override
    public T update(T model) throws ModelNotFoundException {
        findById(model.getId());
        return storage.update(model);
    }

    protected abstract String getModelName();
}
