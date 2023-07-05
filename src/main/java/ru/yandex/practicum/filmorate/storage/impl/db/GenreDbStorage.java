package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.GenreConverter;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("select * from genres order by genre_id", (rs, rn) -> GenreConverter.fromResultSet(rs));
    }

    @Override
    public Optional<Genre> findById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from genres where genre_id = ?",
                    (rs, rn) -> Optional.of(GenreConverter.fromResultSet(rs)),
                    id);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
