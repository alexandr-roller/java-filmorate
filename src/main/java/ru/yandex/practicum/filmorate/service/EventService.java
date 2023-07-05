package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.impl.Event;

import java.util.List;

public interface EventService {
    Event create(Event event);

    List<Event> findByUserId(int userId);
}
