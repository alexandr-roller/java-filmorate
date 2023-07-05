package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectorConverter {
    public static Director fromResultSet(ResultSet resultSet) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("director_id"))
                .name(resultSet.getString("director_name"))
                .build();
    }
}
