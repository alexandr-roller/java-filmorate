package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class InMemoryAbstractStorage<T extends Model> implements Storage<T> {
    private final Map<Integer, T> data = new HashMap<>();
    private int id;

    @Override
    public List<T> findAll() {
        return data.values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public T create(T model) {
        model.setId(++id);
        data.put(id, model);
        return model;
    }

    @Override
    public T update(T model) {
        data.put(model.getId(), model);
        return model;
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
