package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Director {
    private Integer id;
    private String name;

    public Map<String, Object> toMap() {
        Map<String,Object> values = new HashMap<>();
        values.put("DIRECTOR_ID", id);
        values.put("DIRECTOR_NAME", name);
        return values;
    }
}
