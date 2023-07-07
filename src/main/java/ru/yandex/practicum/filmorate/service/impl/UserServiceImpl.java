package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends AbstractService<User> implements UserService {
    private final UserStorage userStorage;
    private final EventService eventService;

    @Autowired
    public UserServiceImpl(UserStorage storage, EventService eventService) {
        super(storage);
        this.userStorage = storage;
        this.eventService = eventService;
    }

    private void changeUserNameIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    protected String getModelName() {
        return "User";
    }

    @Override
    public User create(User user) {
        changeUserNameIfNull(user);
        User createdUser = super.create(user);
        log.info("User is created");
        return createdUser;
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        try {
            User updatedUser = super.update(user);
            log.info("User is updated");
            return updatedUser;
        } catch (ModelNotFoundException ex) {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public void addFriend(int userId, int friendId) throws UserNotFoundException {
        User user = findById(userId);
        User friend = findById(friendId);
        userStorage.addFriend(user, friend);
        log.info("Friend is added");

        eventService.create(
                Event.builder()
                        .userId(userId)
                        .type(Event.EventType.FRIEND)
                        .operation(Event.OperationType.ADD)
                        .entityId(friendId)
                        .build()
        );
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        userStorage.deleteFriend(user, friend);
        log.info("Friend is deleted");

        eventService.create(
                Event.builder()
                        .userId(userId)
                        .type(Event.EventType.FRIEND)
                        .operation(Event.OperationType.REMOVE)
                        .entityId(friendId)
                        .build()
        );
    }

    @Override
    public List<User> getFriends(int userId) {
        findById(userId);
        return userStorage.findFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.findCommonFriends(id, otherId);
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return userStorage.getRecommendations(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        userStorage.deleteUserById(userId);
        log.info("User is deleted");
    }
}
