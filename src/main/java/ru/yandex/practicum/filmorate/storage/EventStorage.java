package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Event;

import java.util.List;

public interface EventStorage {
    List<Event> findByUserId(int userId);

    Event create(Event event);
}
