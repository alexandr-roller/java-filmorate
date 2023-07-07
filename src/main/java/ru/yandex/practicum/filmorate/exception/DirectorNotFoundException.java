package ru.yandex.practicum.filmorate.exception;

public class DirectorNotFoundException extends ModelNotFoundException {
    public DirectorNotFoundException(int id) {
        super("Director id = " + id + " not found", "Director not found");
    }
}
