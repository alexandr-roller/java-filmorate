package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Test;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaController(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    @GetMapping("{id}")
    public Mpa getMpas(@PathVariable("id") int mpaId) {
        return mpaStorage.getMpa(mpaId);
    }
}