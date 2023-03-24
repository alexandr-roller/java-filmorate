package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
