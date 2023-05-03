package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component("UserServiceDao")
public class UserServiceDao implements UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public UserServiceDao(@Qualifier("UserDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("Получен запрос addFriend({}, {})", userId, friendId);
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
        String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID)" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        log.info("Получен запрос removeFriend({}, {})", userId, friendId);
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        log.info("Получен запрос getFriends({})", userId);
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, this::mapRowtoUser, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        log.info("Получен запрос getCommonFriends({}, {})", userId, otherUserId);
        String sql = "SELECT * FROM USERS " +
                "JOIN FRIENDS ON USERS.USER_ID = FRIENDS.FRIEND_ID " +
                "WHERE FRIENDS.USER_ID = ?" +
                "AND FRIEND_ID IN " +
                "(SELECT FRIEND_ID FROM FRIENDS " +
                "WHERE FRIENDS.USER_ID = ?)";
        return jdbcTemplate.query(sql, this::mapRowtoUser, userId, otherUserId);
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
}
