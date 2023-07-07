package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserConverter {

    public static User fromResultSet(ResultSet rs) throws SQLException {
        return User.userBuilder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getDate("birthday")).toLocalDate())
                .build();
    }
}
