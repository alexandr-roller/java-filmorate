package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
public @interface FilmReleaseDate {
    String message() default "must be after 1895-12-28.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
