package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
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
        userStorage.getUser(friendId);
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        log.info("Получен запрос addFriend(userId {}, friendId {})", userId, friendId);
        return userStorage.getUser(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        userStorage.getUser(friendId);
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        log.info("Получен запрос removeFriend(userId {}, friendId {}", userId, friendId);
        return userStorage.getUser(friendId);
    }

    public Collection<User> getFriends(Long userId) {
        log.info("Получен запрос getFriends({})", userId);
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Получен запрос getCommonFriends(userId {}, userID2 {}), возвращён список", userId, otherId);
        return userStorage.getUser(userId).getFriends().stream()
                .filter(userStorage.getUser(otherId).getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}