package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (getUsers().stream().map(User::getEmail).equals(user.getEmail())) {
            log.error("Пользователь с email {} ранее был добавлен", user.getEmail());
            throw new UserAlreadyExistException(String.format("Пользователь %s ранее был добавлен", user));
        }
        checkUser(user);
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Значению имени пользователя присвоено значение поля логин {}", user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        return getUser(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
    }

    @Override
    public User updateUser(User user) {
        getUser(user.getId());
        checkUser(user);
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Обновлены данные пользователя {}", user);
        return getUser(user.getId());
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Получен запрос getUsers");
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::mapRowtoUser);
    }

    @Override
    public User getUser(Long userID) {
        try {
            log.info("Получен запрос getUser({})", userID);
            String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowtoUser, userID);
        } catch (Exception e) {
            log.error("Пользователь c id {} не найден", userID);
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userID));
        }
    }

    private User mapRowtoUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
