package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends ModelNotFoundException {

    public FilmNotFoundException(int id) {
        super("Film id=" + id + " not found", "Film not found");
    }
}
