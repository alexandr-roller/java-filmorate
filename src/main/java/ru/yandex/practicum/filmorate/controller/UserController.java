package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final LocalDate now = LocalDate.now();
    private final Map<Integer, User> users = new HashMap<>();
    private int nexID = 1;

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
         if (checkUser(user)) {
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Значению имени пользователя присвоено значение поля логин");
            }
            user.setId(nexID);
            log.info("Присвоен ID фильму");
            nexID++;
            log.info("Обновлён nextID" + nexID);
            users.put(user.getId(), user);
            log.info("Добавлен пользователь " + user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId()) && checkUser(user)) {
            users.put(user.getId(), user);
            log.info("Обновлены данные пользователя " + user);
        } else {
            log.error("Данный пользователь еще не добавлен");
            throw new ValidationException();
        }
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получен запрос getUsers");
        return users.values();
    }

    private boolean checkUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException();
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException();
        } else if (user.getBirthday().isAfter(now)) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException();
        } else {
            return true;
        }
    }
}
