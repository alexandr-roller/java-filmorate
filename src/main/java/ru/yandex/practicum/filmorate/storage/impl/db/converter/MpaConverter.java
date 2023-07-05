package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Mpa;
import ru.yandex.practicum.filmorate.util.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class MpaConverter {

    public static Mpa fromResultSet(ResultSet resultSet) throws SQLException {
        int mpaId;
        String mpaName;
        Set<String> columnNames = ResultSetUtil.getColumnNames(resultSet);

        if (columnNames.contains("mpa_name")) {
            mpaName = resultSet.getString("mpa_name");
        } else {
            mpaName = resultSet.getString("name");
        }

        if (columnNames.contains("mpa_mpa_id")) {
            mpaId = resultSet.getInt("mpa_mpa_id");
        } else {
            mpaId = resultSet.getInt("mpa_id");
        }

        return new Mpa(mpaId, mpaName);
    }
}
