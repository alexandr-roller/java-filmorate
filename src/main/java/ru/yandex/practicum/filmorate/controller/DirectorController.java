package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorController(@Qualifier("DirectorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    @PostMapping
    public Director addDirector(@RequestBody Director director) {
        return directorStorage.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@RequestBody Director director) {
        return directorStorage.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable("id") Integer directorId) {
        directorStorage.removeDirector(directorId);
    }

    @GetMapping
    public Collection<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable("id") Integer directorId) {
        return directorStorage.getDirector(directorId);
    }
}
