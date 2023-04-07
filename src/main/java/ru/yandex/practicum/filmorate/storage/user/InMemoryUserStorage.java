package ru.yandex.practicum.filmorate.storage.user.userStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final LocalDate now = LocalDate.now();
    private final Map<Integer, User> users = new HashMap<>();
    private int nexID = 1;

    public User addUser(User user) {
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
            throw new ValidationException("Данный пользователь " + user + " еще не добавлен");
        }
        return user;
    }

    public Collection<User> getUsers() {
        log.info("Получен запрос getUsers. Возвращается {} записей", users.size());
        return users.values();
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(now)) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
