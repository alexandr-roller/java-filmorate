package ru.yandex.practicum.filmorate.model.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Model;

import java.sql.Timestamp;

@Data
@Builder
public class Event implements Model {
    @JsonProperty("eventId")
    private int id;

    @Builder.Default
    private long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    private int userId;

    @JsonProperty("eventType")
    private EventType type;

    private OperationType operation;
    private int entityId;

    @RequiredArgsConstructor
    @Getter
    public enum EventType {
        LIKE(1), REVIEW(2), FRIEND(3);

        private final int id;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OperationType {
        REMOVE(1), ADD(2), UPDATE(3);

        private final int id;
    }
}
