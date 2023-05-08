package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component("FilmServiceDao")
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(int filmId, Long userId) {
        log.info("Получен запрос addLike({}, {})", filmId, userId);
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Был добавлен лайк фильму id{} от пользователя id{}", filmId, userId);
    }

    public void removeLike(int filmId, Long userId) {
        log.info("Получен запрос removeLike({}, {})", filmId, userId);
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Был удасён лайк фильма id{} от пользователя id{}", filmId, userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        log.info("Получен запрос getPopularFilms({})", count);
        String sql = "SELECT F.*, M.MPA_NAME from FILMS as F " +
                "INNER JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "LEFT OUTER JOIN (SELECT FILM_ID, COUNT(USER_ID) AS CNT " +
                "FROM FILM_LIKES GROUP BY FILM_ID) AS LK ON " +
                "F.FILM_ID = LK.FILM_ID " +
                "ORDER BY LK.CNT DESC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowtoFilm, count);
    }

    public Film mapRowtoFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(Mpa.builder()
                        .id(resultSet.getInt("MPA_ID"))
                        .name(resultSet.getString("MPA_NAME"))
                        .build())
                .genres(makeGenres(resultSet.getInt("FILM_ID")))
                .build();
    }

    private Set<Genre> makeGenres(int filmId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowGenre, filmId));
    }

    private Genre mapRowGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
