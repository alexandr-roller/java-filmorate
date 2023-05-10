package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Review {
    private Integer reviewId;
    private Integer filmId;
    private Long userId;
    private String content;
    private boolean isPositive;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("REVIEW_ID", reviewId);
        values.put("FILM_ID", filmId);
        values.put("USER_ID", userId);
        values.put("CONTENT", content);
        values.put("IS_POSITIVE", isPositive);
        return values;
    }
}