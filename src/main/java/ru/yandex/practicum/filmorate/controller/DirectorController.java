package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> getDirectors() {
        return directorService.findAll();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable("id") Integer directorId) {
        return directorService.findById(directorId);
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable("id") Integer directorId) {
        directorService.removeDirector(directorId);
    }
}