package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Optional<Review> findById(int reviewId);

    List<Review> findByFilmIdLimited(int filmId, int count);

    List<Review> findAllLimitedTo(int count);

    Review create(Review review);

    Review update(Review review);

    void deleteById(int reviewId);

    void createLike(int reviewId, int userId);

    void createDislike(int reviewId, int userId);

    void deleteLike(int reviewId, int userId);

    void deleteDislike(int reviewId, int userId);
}
