package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.EventConverter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Override
    public List<Event> findByUserId(int userId) {
        if (!userDbStorage.isUserExists(userId)) {
            throw new UserNotFoundException(userId);
        }

        return jdbcTemplate.query("select e.*, " +
                        "et.name as event_type, " +
                        "ot.name as operation_type " +
                        "from events e " +
                        "join event_types et " +
                        "  on et.evtp_id = e.evtp_evtp_id " +
                        "join operation_types ot " +
                        "  on ot.optp_id = e.optp_optp_id " +
                        "where e.user_user_id = ? " +
                        "order by e.timestamp",
                (rs, rn) -> EventConverter.fromResultSet(rs),
                userId);
    }

    @Override
    public Event create(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");

        Map<String, Object> values = Map.of(
                "timestamp", new Timestamp(System.currentTimeMillis()).getTime(),
                "user_user_id", event.getUserId(),
                "evtp_evtp_id", event.getType().getId(),
                "optp_optp_id", event.getOperation().getId(),
                "entity_id", event.getEntityId()
        );

        int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
        event.setId(id);

        return event;
    }
}
