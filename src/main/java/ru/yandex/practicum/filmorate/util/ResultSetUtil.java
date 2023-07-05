package ru.yandex.practicum.filmorate.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ResultSetUtil {
    public static Set<String> getColumnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        Set<String> columnNames = new HashSet<>();

        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(rsMetaData.getColumnLabel(i).toLowerCase());
        }

        return columnNames;
    }
}
