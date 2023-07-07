package ru.yandex.practicum.filmorate.model.impl;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.model.Model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder(builderMethodName = "userBuilder")
public class User implements Model {
    private int id;

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    @Nullable
    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
