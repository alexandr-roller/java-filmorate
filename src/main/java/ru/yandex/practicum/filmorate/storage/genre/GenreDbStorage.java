package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component("GenreDbStorage")
public class GenreDbStorage implements GenreStorage{
    private JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getGenres() {
        log.info("Получень запрос getGenres");
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, this::genreRowToMpa);
    }

    @Override
    public Genre getGenre(Integer genreId) {
        try {
            log.info("Получень запрос getGenre");
            String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::genreRowToMpa, genreId);
        } catch (Exception e) {
            log.error("Жанр c id {} не найден", genreId);
            throw new GenreNotFoundException(String.format("Жанр с id %s не найден", genreId));
        }
    }

    private Genre genreRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
