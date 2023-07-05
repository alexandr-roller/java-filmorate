package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends ModelNotFoundException {

    public UserNotFoundException(int id) {
        super("User id=" + id + " not found", "User not found");
    }
}
