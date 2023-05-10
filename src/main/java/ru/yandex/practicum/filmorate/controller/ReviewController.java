package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewController(@Qualifier("ReviewDbStorage") ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewStorage.addReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        return reviewStorage.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public void removeReview(@PathVariable("reviewId") Integer reviewId) {
        reviewStorage.removeReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable("reviewId") Integer reviewId) {
        return reviewStorage.getReview(reviewId);
    }

    @GetMapping
    public Collection<Review> getReviews(@RequestParam(name = "filmId",required = false) Integer filmId,
                                         @RequestParam(name = "count", defaultValue = "10") Integer count) {
        if (filmId == null) {
            return reviewStorage.getReviews(count);
        } else {
            return reviewStorage.getFilmReviews(filmId, count);
        }
    }
}