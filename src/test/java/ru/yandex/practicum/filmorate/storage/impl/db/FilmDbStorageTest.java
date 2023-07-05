package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Mpa;
import ru.yandex.practicum.filmorate.model.impl.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/test-data.sql"})
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void shouldCreate() {
        Film film = Film.filmBuilder()
                .name("Test film")
                .description("Test film")
                .releaseDate(LocalDate.now().minusDays(5))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .build();

        assertThat(filmDbStorage.create(film)).hasFieldOrPropertyWithValue("id", 3);
    }

    @Test
    public void shouldUpdate() {
        Film film = Film.filmBuilder()
                .id(1)
                .name("New name")
                .description("Test film")
                .releaseDate(LocalDate.now().minusDays(5))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .build();

        assertThat(filmDbStorage.update(film)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    public void shouldFindAll() {
        List<Film> filmList = filmDbStorage.findAll();

        assertThat(filmList).isNotEmpty();
        assertThat(filmList).hasSize(2);
    }

    @Test
    public void shouldFindById() {
        Optional<Film> filmOptional = filmDbStorage.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void shouldAddLike() {
        Film film = filmDbStorage.findById(1).orElseThrow(() -> new FilmNotFoundException(1));
        User user = userDbStorage.findById(2).orElseThrow(() -> new UserNotFoundException(2));

        filmDbStorage.addLike(film, user);

        assertThat(filmDbStorage.getNumberOfLikes(1)).isEqualTo(2);
    }

    @Test
    public void shouldDeleteLike() {
        Film film = filmDbStorage.findById(1).orElseThrow(() -> new FilmNotFoundException(1));
        User user = userDbStorage.findById(1).orElseThrow(() -> new UserNotFoundException(1));

        filmDbStorage.deleteLike(film, user);

        assertThat(filmDbStorage.getNumberOfLikes(1)).isEqualTo(0);
    }
}