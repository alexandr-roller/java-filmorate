package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventService eventService;

    @Override
    public Review findById(int id) {
        return reviewStorage.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public List<Review> findByFilmIdLimited(int filmId, int count) {
        return reviewStorage.findByFilmIdLimited(filmId, count);
    }

    @Override
    public List<Review> findAllLimitedTo(int count) {
        return reviewStorage.findAllLimitedTo(count);
    }

    @Override
    public Review create(Review review) {
        Review createdReview = reviewStorage.create(review);
        log.info("Review is created");

        eventService.create(
                Event.builder()
                        .userId(review.getUserId())
                        .type(Event.EventType.REVIEW)
                        .operation(Event.OperationType.ADD)
                        .entityId(review.getId())
                        .build()
        );

        return createdReview;
    }

    @Override
    public Review update(Review review) {
        Review updatedReview = reviewStorage.update(review);
        log.info("Review is updated");

        eventService.create(
                Event.builder()
                        .userId(updatedReview.getUserId())
                        .type(Event.EventType.REVIEW)
                        .operation(Event.OperationType.UPDATE)
                        .entityId(updatedReview.getId())
                        .build()
        );

        return updatedReview;
    }

    @Override
    public void deleteById(int reviewId) {
        int userId = findById(reviewId).getUserId();
        reviewStorage.deleteById(reviewId);
        log.info("Review is deleted");

        eventService.create(
                Event.builder()
                        .userId(userId)
                        .type(Event.EventType.REVIEW)
                        .operation(Event.OperationType.REMOVE)
                        .entityId(reviewId)
                        .build()
        );
    }

    @Override
    public void createLike(int reviewId, int userId) {
        reviewStorage.createLike(reviewId, userId);
        log.info("Like for review is added");
    }

    @Override
    public void createDislike(int reviewId, int userId) {
        reviewStorage.createDislike(reviewId, userId);
        log.info("Dislike for review is added");
    }

    @Override
    public void deleteLike(int reviewId, int userId) {
        reviewStorage.deleteLike(reviewId, userId);
        log.info("Like for review is deleted");
    }

    @Override
    public void deleteDislike(int reviewId, int userId) {
        reviewStorage.deleteDislike(reviewId, userId);
        log.info("Dislike for review is deleted");
    }
}
