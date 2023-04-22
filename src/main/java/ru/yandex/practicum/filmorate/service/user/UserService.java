package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        if (new HashSet<>(userStorage.getUsers().stream().map(User::getId)
                .collect(Collectors.toList())).containsAll(List.of(userId, friendId))) {
            userStorage.getUser(userId).getFriends().add(friendId);
            userStorage.getUser(friendId).getFriends().add(userId);
            log.info("Получен запрос addFriend(userId-{}, friendId-{})", userId, friendId);
            return userStorage.getUser(friendId);
        } else {
            log.error("Ошибка с полем userId или friendId");
            throw new IncorrectParameterException("userId или friendId");
        }
    }

    public User removeFriend(Long userId, Long friendId) {
        if (new HashSet<>(userStorage.getUsers().stream().map(User::getId)
                .collect(Collectors.toList())).containsAll(List.of(userId, friendId))) {
            userStorage.getUser(userId).getFriends().remove(friendId);
            userStorage.getUser(friendId).getFriends().remove(userId);
            log.info("Получен запрос removeFriend(userId-{}, friendId-{}", userId, friendId);
            return userStorage.getUser(friendId);
        } else {
            log.error("Ошибка с полем userId или friendId");
            throw new IncorrectParameterException("userId или friendId");
        }
    }

    public Collection<User> getFriends(Long userId) {
        log.info("Получен запрос getFriends({})", userId);
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long userId2) {
        log.info("Получен запрос getCommonFriends(userId-{}, userID2-{}), возвращён список", userId, userId2);
        return userStorage.getUser(userId).getFriends().stream()
                .filter(userStorage.getUser(userId2).getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}