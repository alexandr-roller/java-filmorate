package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Mpa findById(int id) {
        return mpaStorage.findById(id).orElseThrow(() -> new MpaNotFoundException(id));
    }
}
