package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.ReviewConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private static final String BASE_SELECT = "select r.*," +
            " coalesce(sum(case when rg.rev_rev_id is not null and rg.is_positive then 1" +
            "                   when rg.rev_rev_id is not null and not rg.is_positive then -1 end), 0) useful " +
            " from reviews r" +
            " left join review_grades rg" +
            "        on rg.rev_rev_id = r.rev_id " +
            " <WHERE> " +
            " group by r.rev_id " +
            " order by useful desc " +
            " <LIMIT>";
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Override
    public Optional<Review> findById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    BASE_SELECT.replace("<WHERE>", " where r.rev_id = ?")
                            .replace("<LIMIT>", ""),
                    (rs, rn) -> Optional.of(ReviewConverter.fromResultSet(rs)),
                    id);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findByFilmIdLimited(int filmId, int count) {
        return jdbcTemplate.query(
                BASE_SELECT.replace("<WHERE>", "where r.film_film_id = ?")
                        .replace("<LIMIT>", "limit ?"),
                (rs, rn) -> ReviewConverter.fromResultSet(rs),
                filmId,
                count);
    }

    @Override
    public List<Review> findAllLimitedTo(int count) {
        return jdbcTemplate.query(
                BASE_SELECT.replace("<WHERE>", "")
                        .replace("<LIMIT>", "limit ?"),
                (rs, rn) -> ReviewConverter.fromResultSet(rs),
                count);
    }

    @Override
    public Review create(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("rev_id");

        Map<String, Object> values = Map.of(
                "content", review.getContent(),
                "is_positive", review.getPositive(),
                "user_user_id", review.getUserId(),
                "film_film_id", review.getFilmId()
        );

        if (!filmDbStorage.isFilmExists(review.getFilmId())) {
            throw new FilmNotFoundException(review.getFilmId());
        }

        if (!userDbStorage.isUserExists(review.getUserId())) {
            throw new UserNotFoundException(review.getUserId());
        }

        int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
        review.setId(id);

        return review;
    }

    @Override
    public Review update(Review review) {
        String sqlQuery = "update reviews set " +
                "content = ?, is_positive = ? " +
                "where rev_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getPositive(),
                review.getId());

        return findById(review.getId()).orElseThrow(() -> new ReviewNotFoundException(review.getId()));
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("delete from reviews where rev_id = ?", id);
    }

    @Override
    public void createLike(int reviewId, int userId) {
        createGrade(reviewId, userId, true);
    }

    @Override
    public void createDislike(int reviewId, int userId) {
        createGrade(reviewId, userId, false);
    }

    @Override
    public void deleteLike(int reviewId, int userId) {
        deleteGrade(reviewId, userId, true);
    }

    @Override
    public void deleteDislike(int reviewId, int userId) {
        deleteGrade(reviewId, userId, false);
    }

    private void createGrade(int reviewId, int userId, boolean isPositive) {
        jdbcTemplate.update("insert into review_grades values(?,?,?)", reviewId, userId, isPositive);
    }

    private void deleteGrade(int reviewId, int userId, boolean isPositive) {
        jdbcTemplate.update("delete from review_grades where rev_rev_id = ? and user_user_id = ? and is_positive = ?",
                reviewId, userId, isPositive);
    }
}
