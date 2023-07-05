package ru.yandex.practicum.filmorate.exception.response;

import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String description;
}
