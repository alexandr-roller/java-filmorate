package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends ModelNotFoundException {

    public GenreNotFoundException(int id) {
        super("Genre id=" + id + " not found", "Genre not found");
    }
}
