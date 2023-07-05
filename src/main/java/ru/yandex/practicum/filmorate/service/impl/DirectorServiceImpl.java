package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

@Service
@Slf4j
@Component("DirectorServiceImpl")
public class DirectorServiceImpl extends AbstractService<Director> implements DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorServiceImpl(DirectorStorage directorStorage) {
        super(directorStorage);
        this.directorStorage = directorStorage;
    }

    @Override
    protected String getModelName() {
        return "Director";
    }

    @Override
    public Director create(Director director) {
        Director createdDirector = directorStorage.create(director);
        log.info("Director is created");
        return createdDirector;
    }

    @Override
    public Director update(Director director) {
        try {
            Director updatedDirector = super.update(director);
            log.info("Director is updated");
            return updatedDirector;
        } catch (ModelNotFoundException ex) {
            throw new DirectorNotFoundException(director.getId());
        }
    }

    @Override
    public void removeDirector(Integer directorId) {
        directorStorage.removeDirector(directorId);
        log.info("Director is removed");
    }
}
