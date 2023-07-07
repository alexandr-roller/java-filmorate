package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;

    @Override
    public Event create(Event event) {
        return eventStorage.create(event);
    }

    @Override
    public List<Event> findByUserId(int userId) {
        return eventStorage.findByUserId(userId);
    }
}
