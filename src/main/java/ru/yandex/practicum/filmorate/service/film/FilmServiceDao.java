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
public class FilmServiceDao implements FilmService{
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;


    public FilmServiceDao(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                          @Qualifier("UserDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, Long userId) {
        log.info("Получен запрос addLike({}, {})", filmId, userId);
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, Long userId) {
        log.info("Получен запрос removeLike({}, {})", filmId, userId);
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        log.info("Получен запрос getPopularFilms({})", count);
        if (count == null || count <= 0) {
            count = 10;
        }
        String sql = "SELECT * FROM FILMS WHERE FILM_ID IN ( " +
                "SELECT FILM_ID FROM FILM_LIKES " +
                "GROUP BY FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?)";
        return jdbcTemplate.query(sql, this::mapRowtoFilm, count);
    }

    public Film mapRowtoFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(makeMpa(resultSet.getInt("MPA_ID")))
                .genres(makeGenres(resultSet.getInt("FILM_ID")))
                .rate(resultSet.getInt("RATE"))
                .build();
    }

    private Mpa makeMpa(int mpaId) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowtoMpa, mpaId);
    }

    private Set<Genre> makeGenres(int filmId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowGenre, filmId));
    }

    private Mpa mapRowtoMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }

    private Genre mapRowGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
