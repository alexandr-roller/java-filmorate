package ru.yandex.practicum.filmorate.model.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Model;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review implements Model {
    @JsonProperty("reviewId")
    private int id;

    private String content;

    @JsonProperty("isPositive")
    @NotNull
    private Boolean positive;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;

    private int useful;
}
