package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmControllerTest {
    FilmController controller = new FilmController();
    Film film = Film.builder()
            .name("Name")
            .description("Description")
            .releaseDate(LocalDate.of(2023, 1, 1))
            .duration(100)
            .build();

    @Test
    void addFilm() throws ValidationException {
        controller.addFilm(film);
        assertFalse(controller.getFilms().isEmpty());
    }

//    @Test
//    void addFilmWhenFallName() {
//        film.setName("");
//    }

//    @Test
//    void updateFilm() throws ValidationException {
//        controller.addFilm(film);
//        Film newFilm = Film.builder()
//                .id(controller.getFilms().get(0).getId())
//                .name("New name")
//                .description("Description")
//                .releaseDate(LocalDate.of(2023, 2, 2))
//                .duration(150)
//                .build();
//        controller.updateFilm(newFilm);
//        assertEquals("New name", controller.getFilms().get(0).getName());
//
//        newFilm.setId(10);
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> controller.updateFilm(newFilm)
//        );
//        assertEquals(1, controller.getFilms().get(0).getId());
//    }
}