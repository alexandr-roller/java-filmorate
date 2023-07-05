package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserStorage userStorage;

    @Test
    public void shouldCreateUser() {
        User user = User.userBuilder()
                .id(1)
                .email("example@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Mockito.when(userStorage.create(user)).thenReturn(user);

        assertEquals(user, userService.create(user));
    }

    @Test
    public void shouldUpdateUser() {
        User user = User.userBuilder()
                .id(1)
                .email("example@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Mockito.doReturn(Optional.of(user)).when(userStorage).findById(1);

        User userUpd = User.userBuilder()
                .id(1)
                .email("example@mail.ru")
                .login("new_login")
                .name("name").birthday(LocalDate.of(1990, 1, 1))
                .build();

        Mockito.when(userStorage.update(userUpd)).thenReturn(userUpd);

        assertEquals(userUpd, userService.update(userUpd));
    }

    @Test
    public void shouldNotUpdateUser() {
        User user = User.userBuilder()
                .id(1)
                .email("example@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(UserNotFoundException.class, () -> userService.update(user));
        assertTrue(userService.findAll().isEmpty());
    }
}
