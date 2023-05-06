package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final UserService userService;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;

    User user1 = User.builder()
            .login("login1")
            .birthday(LocalDate.of(2000, 1, 1))
            .email("mail@mail.ru")
            .build();
    User user2 = User.builder()
            .login("login2")
            .birthday(LocalDate.of(2000, 2, 2))
            .email("mail2@mail.ru")
            .build();

    List<Genre> genres = List.of(
            Genre.builder()
                    .id(1)
                    .name("Комедия")
                    .build(),
            Genre.builder()
                    .id(2)
                    .name("Драма")
                    .build(),
            Genre.builder()
                    .id(3)
                    .name("Мультфильм")
                    .build(),
            Genre.builder()
                    .id(4)
                    .name("Триллер")
                    .build(),
            Genre.builder()
                    .id(5)
                    .name("Документальный")
                    .build(),
            Genre.builder()
                    .id(6)
                    .name("Боевик")
                    .build()
    );

    List<Mpa> mpa = List.of(Mpa.builder()
                    .id(1)
                    .name("G")
                    .build(),
            Mpa.builder()
                    .id(2)
                    .name("RG")
                    .build(),
            Mpa.builder()
                    .id(3)
                    .name("RG-13")
                    .build(),
            Mpa.builder()
                    .id(4)
                    .name("R")
                    .build(),
            Mpa.builder()
                    .id(5)
                    .name("NS-17")
                    .build()
    );

    Film film1 = Film.builder()
            .name("name1")
            .description("description")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
            .mpa(mpa.get(1))
            .genres(Set.of(genres.get(1)))
            .build();
    Film film2 = Film.builder()
            .name("name2")
            .description("description2")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
            .mpa(mpa.get(2))
            .genres(Set.of(genres.get(3)))
            .build();

    @Test
    void contextLoads() {
        user1 = userStorage.addUser(user1);
        user2 = userStorage.addUser(user2);
        film1 = filmStorage.addFilm(film1);
        film2 = filmStorage.addFilm(film2);

        assertTrue(userService.getCommonFriends(1L, 2L).isEmpty());
        filmService.addLike(film1.getId(), user1.getId());
        assertEquals(2, filmService.getPopularFilms(5).size());
        filmService.removeLike(film1.getId(), user1.getId());

        userService.addFriend(user1.getId(), user2.getId());
        userService.getFriends(user1.getId());
        userService.addFriend(user2.getId(), user1.getId());
        userService.getCommonFriends(user1.getId(), user2.getId());
    }
}
