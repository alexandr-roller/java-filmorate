package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("InMemoryUserService")
@Slf4j
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    public InMemoryUserService(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        boolean isFriend = userStorage.getUser(friendId).getFriends().contains(userId);
        userStorage.getUser(userId).getFriends().add(friendId);
//        userStorage.getUser(friendId).getFriends().add(userId);
        log.info("Получен запрос addFriend(userId {}, friendId {})", userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        boolean isFriend = userStorage.getUser(friendId).getFriends().contains(userId);
            userStorage.getUser(userId).getFriends().remove(friendId);
//            userStorage.getUser(friendId).getFriends().remove(userId);
        log.info("Получен запрос removeFriend(userId {}, friendId {}", userId, friendId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        log.info("Получен запрос getFriends({})", userId);
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Получен запрос getCommonFriends(userId {}, userID2 {}), возвращён список", userId, otherId);
        return userStorage.getUser(userId).getFriends().stream()
                .filter(userStorage.getUser(otherId).getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}