package ru.yandex.practicum.filmorate.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Model;

@Data
@AllArgsConstructor
public class Genre implements Model {
    private int id;
    private String name;
}
