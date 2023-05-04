package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final LocalDate now = LocalDate.now();
    private final Map<Long, User> users = new HashMap<>();
    private Long nexID = 1L;

    public User addUser(User user) {
        if (users.containsValue(user)) {
            log.error("Пользователь {} ранее был добавлен", user);
            throw new UserAlreadyExistException(String.format("Пользователь %s ранее был добавлен", user));
        }
        checkUser(user);
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Значению имени пользователя присвоено значение поля логин {}", user.getLogin());
        }
        user.setId(nexID);
        log.info("Присвоен ID фильму {}", nexID);
        nexID++;
        log.info("Обновлён nextID {}", nexID);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            checkUser(user);
            users.put(user.getId(), user);
            log.info("Обновлены данные пользователя {}", user);
        } else {
            log.error("Данный пользователь {} еще не добавлен", user);
            throw new UserNotFoundException("Данный пользователь " + user + " еще не добавлен");
        }
        return user;
    }

    public Collection<User> getUsers() {
        log.info("Получен запрос getUsers. Возвращается {} записей", users.size());
        return users.values();
    }

    public User getUser(Long userID) {
        if (users.containsKey(userID)) {
            log.info("Получен запрос getUser({}), Вернулся {}", userID, users.get(userID));
            return users.get(userID);
        } else {
            log.error("Пользователь c id {} не найден", userID);
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userID));
        }
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(now)) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}