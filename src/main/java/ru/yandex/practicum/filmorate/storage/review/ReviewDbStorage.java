package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component("ReviewDbStorage")
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate,
                           @Qualifier("UserDbStorage") UserStorage userStorage,
                           @Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public Review addReview(Review review) {
        log.info("Получен запрос addReview({})", review);
        userStorage.getUser(review.getUserId());
        filmStorage.getFilm(review.getFilmId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("REVIEWS")
                .usingGeneratedKeyColumns("REVIEW_ID");
        review.setReviewId(simpleJdbcInsert.executeAndReturnKey(review.toMap()).intValue());
        log.info("Был добавлен отзыв к фильму {} от пользователя {}", review.getFilmId(), review.getUserId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        log.info("Получен запрос updateReview({})", review);
        getReview(review.getReviewId());
        userStorage.getUser(review.getUserId());
        filmStorage.getFilm(review.getFilmId());
        String sql = "UPDATE REVIEWS SET FILM_ID = ?, USER_ID = ?, " +
                "CONTENT = ?, IS_POSITIVE = ? " +
                "WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql,
                review.getFilmId(),
                review.getUserId(),
                review.getContent(),
                review.isPositive(),
                review.getReviewId());
        log.info("Обновлены данные отзыва {}", review);
        return getReview(review.getReviewId());
    }

    @Override
    public void removeReview(Integer reviewId) {
        log.info("Получен запрос removeReview({})", reviewId);
        String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, reviewId);
        log.info("Отзыв {} был удалён", reviewId);
    }

    @Override
    public Review getReview(Integer reviewId) {
        try {
        log.info("Получен запрос getReview({})", reviewId);
        String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowtoReview, reviewId);
        } catch (Exception e) {
            log.error("Отзыв c id {} не найден", reviewId);
            throw new UserNotFoundException(String.format("Отзыв с id %s не найден", reviewId));
        }
    }

    @Override
    public Collection<Review> getReviews(Integer count) {
        log.info("Получен запрос getReview()");
        String sql = "SELECT * FROM REVIEWS LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowtoReview, count);
    }

    @Override
    public Collection<Review> getFilmReviews(Integer filmId, Integer count) {
        log.info("Получен запрос getFilmReviews({})", filmId);
        String sql = "SELECT * FROM REVIEWS WHERE FILM_ID = ? LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowtoReview, filmId, count);
    }

    public Review mapRowtoReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getInt("REVIEW_ID"))
                .filmId(resultSet.getInt("FILM_ID"))
                .userId(resultSet.getLong("USER_ID"))
                .content(resultSet.getString("CONTENT"))
                .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                .build();
    }
}
