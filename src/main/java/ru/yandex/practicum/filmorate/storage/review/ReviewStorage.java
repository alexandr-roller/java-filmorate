package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage {
    Review addReview(Review review);

    Review updateReview(Review review);

    void removeReview(Integer reviewId);

    Review getReview(Integer reviewId);

    Collection<Review> getReviews(Integer count);

    Collection<Review> getFilmReviews(Integer filmId, Integer count);
}
