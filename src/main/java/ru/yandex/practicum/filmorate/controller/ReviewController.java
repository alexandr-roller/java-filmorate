package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping(value = "/{id}")
    public Review findById(@PathVariable int id) {
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> findByFilmId(@RequestParam(required = false) Integer filmId,
                                     @RequestParam(defaultValue = "10") int count) {
        if (filmId != null) {
            return reviewService.findByFilmIdLimited(filmId, count);
        } else {
            return reviewService.findAllLimitedTo(count);
        }

    }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@RequestBody Review review) {
        return reviewService.update(review);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void createLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.createLike(id, userId);
    }

    @PutMapping(value = "/{id}/dislike/{userId}")
    public void createDislike(@PathVariable int id, @PathVariable int userId) {
        reviewService.createDislike(id, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable int id) {
        reviewService.deleteById(id);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteDislike(id, userId);
    }
}
