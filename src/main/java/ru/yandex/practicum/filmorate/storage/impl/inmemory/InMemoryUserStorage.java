package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends InMemoryAbstractStorage<User> implements UserStorage {
    private final Set<Pair<Integer, Integer>> friends = new HashSet<>();

    @Override
    public void addFriend(User user, User friend) {
        friends.add(Pair.of(user.getId(), friend.getId()));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        friends.remove(Pair.of(user.getId(), friend.getId()));
    }

    @Override
    public List<User> findFriends(int userId) {
        return friends.stream()
                .filter(p -> p.getFirst() == userId)
                .map(Pair::getSecond)
                .map(id -> findById(id).orElseThrow(() -> new UserNotFoundException(id)))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        return findFriends(id).stream()
                .filter(u -> findFriends(otherId).contains(u))
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return null;
    }

    @Override
    public void deleteUserById(int userId) {
    }
}
