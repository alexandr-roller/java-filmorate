package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmServiceDao;
import ru.yandex.practicum.filmorate.service.user.UserServiceDao;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final UserServiceDao userService;
    private final FilmDbStorage filmStorage;
    private final FilmServiceDao filmService;

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

//    List<Genre> genres = List.of(new Genre(1, "Комедия"),
//            new Genre(2, "Драма"),
//            new Genre(3, "Мультфильм"),
//            new Genre(4, "Триллер"),
//            new Genre(5, "Документальный"),
//            new Genre(5, "Боевик"));
//
//    List<Mpa> mpa = List.of(new Mpa(1, "G"),
//            new Mpa(2, "PG"),
//            new Mpa(3, "PG-13"),
//            new Mpa(4, "R"),
//            new Mpa(5, "NC-17"));

    Film film1 = Film.builder()
            .name("name1")
            .description("description")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
//            .mpa(new Mpa(1, "G"))
//            .genres(Set.of(genre1, genre2)))
            .build();
    Film film2 = Film.builder()
            .name("name2")
            .description("description2")
            .duration(100)
            .releaseDate(LocalDate.of(2020, 8, 8))
            .build();

//    @Test
//    void contextLoads() {
//        userStorage.addUser(user1);
//        userStorage.addUser(user2);
//        filmStorage.addFilm(film1);
//        filmStorage.addFilm(film2);
//
//        assertTrue(userService.getCommonFriends(1L, 2L).isEmpty());
//        filmService.addLike(film1.getId(), user1.getId());
//        assertTrue(filmStorage.getFilm(film1.getId()).getLikes().contains(user1.getId()));
//        assertEquals(2, filmService.getPopularFilms(5).size());
//        filmService.removeLike(film1.getId(), user1.getId());
//
//        userService.addFriend(user1.getId(), user2.getId());
//        userService.getFriends(user1.getId());
//        userService.addFriend(user2.getId(), user1.getId());
//        userService.getCommonFriends(user1.getId(), user2.getId());
//    }
}
