package ru.yandex.practicum.filmorate.model.impl;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Model;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Director implements Model {
    private int id;
    @NotBlank
    private String name;
}
