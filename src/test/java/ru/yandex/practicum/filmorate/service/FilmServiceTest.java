package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {
    @InjectMocks
    private FilmServiceImpl filmService;

    @Mock
    private FilmStorage filmStorage;

    @Test
    public void shouldCreateFilm() {
        Film film = Film.filmBuilder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(50)
                .build();

        Mockito.when(filmStorage.create(film)).thenReturn(film);

        assertEquals(film, filmService.create(film));
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = Film.filmBuilder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(50)
                .build();

        Film filmUpd = Film.filmBuilder()
                .id(1)
                .name("Film")
                .description("New description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(50)
                .build();

        Mockito.doReturn(Optional.of(film)).when(filmStorage).findById(1);
        Mockito.when(filmStorage.update(filmUpd)).thenReturn(filmUpd);

        assertEquals(filmUpd, filmService.update(filmUpd));
    }

    @Test
    public void shouldNotUpdateFilm() {
        Film film = Film.filmBuilder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(50)
                .build();

        assertThrows(FilmNotFoundException.class, () -> filmService.update(film));
        assertTrue(filmService.findAll().isEmpty());
    }
}
