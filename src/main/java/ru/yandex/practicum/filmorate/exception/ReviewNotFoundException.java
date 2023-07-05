package ru.yandex.practicum.filmorate.exception;

public class ReviewNotFoundException extends ModelNotFoundException {
    public ReviewNotFoundException(int id) {
        super("Review id=" + id + " not found", "Review not found");
    }
}
