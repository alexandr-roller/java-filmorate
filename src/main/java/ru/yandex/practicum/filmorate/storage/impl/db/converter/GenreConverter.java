package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.util.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class GenreConverter {

    public static Genre fromResultSet(ResultSet resultSet) throws SQLException {
        int genreId;
        String genreName;
        Set<String> columnNames = ResultSetUtil.getColumnNames(resultSet);

        if (columnNames.contains("genre_name")) {
            genreName = resultSet.getString("genre_name");
        } else {
            genreName = resultSet.getString("name");
        }

        if (columnNames.contains("genre_genre_id")) {
            genreId = resultSet.getInt("genre_genre_id");
        } else {
            genreId = resultSet.getInt("genre_id");
        }

        return new Genre(genreId, genreName);
    }
}
