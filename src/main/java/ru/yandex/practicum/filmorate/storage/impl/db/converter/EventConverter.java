package ru.yandex.practicum.filmorate.storage.impl.db.converter;

import ru.yandex.practicum.filmorate.model.impl.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventConverter {
    public static Event fromResultSet(ResultSet rs) throws SQLException {
        return Event.builder()
                .id(rs.getInt("event_id"))
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("user_user_id"))
                .type(Event.EventType.valueOf(rs.getString("event_type").toUpperCase()))
                .operation(Event.OperationType.valueOf(rs.getString("operation_type").toUpperCase()))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
