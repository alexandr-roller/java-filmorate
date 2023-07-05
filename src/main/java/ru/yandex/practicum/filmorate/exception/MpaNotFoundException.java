package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends ModelNotFoundException {

    public MpaNotFoundException(int id) {
        super("Mpa id=" + id + " not found", "Mpa not found");
    }
}
