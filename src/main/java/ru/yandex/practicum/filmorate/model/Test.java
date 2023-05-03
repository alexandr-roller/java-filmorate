package ru.yandex.practicum.filmorate.model;

import java.util.List;

public class Test {
    public static List<Genre> genres = List.of(new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(5, "Боевик"));

    public static List<Mpa> mpa = List.of(new Mpa(1, "G"),
            new Mpa(2, "PG"),
            new Mpa(3, "PG-13"),
            new Mpa(4, "R"),
            new Mpa(5, "NC-17"));
}
