package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {
    UserController controller = new UserController();

    @Test
    void addUser() throws ValidationException {
        User user = User.builder()
                .email("test@test")
                .login("login")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();

        controller.addUser(user);
        assertFalse(controller.getUsers().isEmpty());
    }
}