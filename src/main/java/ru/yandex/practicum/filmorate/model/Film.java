package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NonNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Mpa mpa;
//    private Integer rate;
    private Set<Genre> genres;
    @JsonIgnore
    private Set<Long> likes;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_ID", id);
        values.put("FILM_NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        values.put("MPA_ID", mpa.getId());
//        values.put("RATE", rate);
        return values;
    }
}