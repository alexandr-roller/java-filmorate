package ru.yandex.practicum.filmorate.storage.impl.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.UserConverter;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplateNamed;
    private final FilmDbStorage filmDbStorage;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from users order by user_id", (rs, rn) -> UserConverter.fromResultSet(rs));
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from users where user_id = ?",
                    (rs, rn) -> Optional.of(UserConverter.fromResultSet(rs)),
                    id);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> values = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", Optional.ofNullable(user.getName()).orElse(""),
                "birthday", user.getBirthday()
        );

        int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbcTemplate.update("insert into friends values(?, ?, 2)", user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcTemplate.update("delete from friends where first_user_id = ? and second_user_id = ?",
                user.getId(),
                friend.getId());
    }

    @Override
    public List<User> findFriends(int id) {
        return jdbcTemplate.query("select " +
                        " u.* " +
                        " from friends f " +
                        " join users u " +
                        "   on u.user_id = f.second_user_id " +
                        " where f.first_user_id = ? " +
                        "   and f.frstat_frstat_id = 2 " +
                        " order by u.user_id",
                (rs, rn) -> UserConverter.fromResultSet(rs),
                id);
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        return jdbcTemplate.query("select " +
                        " u.* " +
                        " from (" +
                        "     select f.second_user_id from friends f where f.first_user_id = ? and f.frstat_frstat_id" +
                        " = 2 " +
                        "     intersect " +
                        "     select f.second_user_id from friends f where f.first_user_id = ? and f.frstat_frstat_id" +
                        " = 2 " +
                        " ) fr " +
                        " join users u " +
                        "   on u.user_id = fr.second_user_id " +
                        " order by u.user_id",
                (rs, rn) -> UserConverter.fromResultSet(rs),
                id,
                otherId);
    }

    @Override
    public void deleteUserById(int userId) {
        jdbcTemplate.update("DELETE FROM users  WHERE user_id = ?", userId);
    }

    public boolean isUserExists(int userId) {
        Integer usersCount = jdbcTemplate.queryForObject(
                "select count(*) cnt from users where user_id = ?",
                Integer.class,
                userId
        );
        return usersCount != null && usersCount == 1;
    }

    public List<Film> getRecommendations(int userId) {
        // запрос в бд на получение id фильмов, которые лайкнул пользователь
        String queryForLikedFilms = "select film_film_id from likes where user_user_id = ?";
        List<Integer> idFilmsLiked = jdbcTemplate.queryForList(queryForLikedFilms, Integer.class, userId);
        if (idFilmsLiked.isEmpty()) {
            return Collections.emptyList();
        }

        // запрос в бд на получение id других пользователей, которые тоже лайкнули эти фильмы
        String queryForUsersWithSameInterests = "select user_user_id from likes where film_film_id in (:ids) and " +
                "user_user_id != :userId";
        List<Integer> usersIdWithSameInterests = jdbcTemplateNamed.queryForList(queryForUsersWithSameInterests,
                Map.of("ids", idFilmsLiked, "userId", userId), Integer.class);

        // кладу id этих пользователей в мапу, где ключ это id, а значение это количество совпадающих лайков
        Map<Integer, Integer> usersIdAndFreq = new HashMap<>();
        for (Integer otherUserId : usersIdWithSameInterests) {
            if (!usersIdAndFreq.containsKey(otherUserId)) {
                usersIdAndFreq.put(otherUserId, 1);
            } else {
                usersIdAndFreq.put(otherUserId, usersIdAndFreq.get(otherUserId) + 1);
            }
        }
        if (usersIdAndFreq.isEmpty()) {
            return Collections.emptyList();
        }

        // преобразую мапу в лист с id пользователей в порядке убывания совпадения интересов
        List<Integer> usersIdByFreqOrder = usersIdAndFreq.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry<Integer, Integer>::getValue).reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // запрос в бд на получение id всех фильмов, которые лайкнули эти пользователи. Должен сохраняться порядок
        List<Integer> filmsIdForRecommendation = new ArrayList<>();
        for (Integer otherUserId : usersIdByFreqOrder) {
            filmsIdForRecommendation.addAll(jdbcTemplate.queryForList(queryForLikedFilms, Integer.class, otherUserId));
        }
        // удаляю из списка id фильмов те, которые наш пользователь уже лайкнул
        filmsIdForRecommendation.removeAll(idFilmsLiked);

        // запрос в бд на получение фильмов по id
        return filmDbStorage.findByIds(filmsIdForRecommendation);
    }
}
