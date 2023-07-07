package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewConverter {
    public static Review fromResultSet(ResultSet rs) throws SQLException {
        return Review.builder()
                .id(rs.getInt("rev_id"))
                .content(rs.getString("content"))
                .positive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_user_id"))
                .filmId(rs.getInt("film_film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    public static List<Review> listFromResultSet(ResultSet rs) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        Review currentReview = null;
        int useful = 0;

        while (rs.next()) {
            if (currentReview == null || currentReview.getId() != rs.getInt("rev_id")) {
                currentReview = fromResultSet(rs);
                useful = 0;
                reviews.add(currentReview);
            }
            if (rs.getInt("grade_rev_id") != 0) {
                if (rs.getBoolean("is_positive_grade")) {
                    useful++;
                } else {
                    useful--;
                }
            }
            currentReview.setUseful(useful);
        }

        return reviews;
    }
}
