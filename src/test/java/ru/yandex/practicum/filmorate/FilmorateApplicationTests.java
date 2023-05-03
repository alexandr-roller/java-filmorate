package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    FilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage, userStorage);
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
    Film film1 = Film.builder()
            .name("name1")
            .description("description")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
            .build();
    Film film2 = Film.builder()
            .name("name2")
            .description("description2")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
            .build();

    @Test
    void contextLoads() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        assertTrue(userService.getCommonFriends(1L, 2L).isEmpty());
        filmService.addLike(film1.getId(), user1.getId());
        assertTrue(filmStorage.getFilm(film1.getId()).getLikes().contains(user1.getId()));
        assertEquals(2, filmService.getPopularFilms(5).size());
        filmService.removeLike(film1.getId(), user1.getId());

        userService.addFriend(user1.getId(), user2.getId());
        userService.getFriends(user1.getId());
        userService.addFriend(user2.getId(), user1.getId());
        userService.getCommonFriends(user1.getId(), user2.getId());
    }
}
