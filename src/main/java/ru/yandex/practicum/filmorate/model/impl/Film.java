package ru.yandex.practicum.filmorate.model.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.validator.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(builderMethodName = "filmBuilder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film implements Model {
    private int id;
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @FilmReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres = new HashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Director> directors = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private Mpa mpa;
}
