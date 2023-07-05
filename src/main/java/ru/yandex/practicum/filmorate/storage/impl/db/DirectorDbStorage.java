package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.DirectorConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("DirectorDbStorage")
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");

        Map<String,Object> values = Map.of(
                "director_id", director.getId(),
                "director_name", director.getName()
        );

        director.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE directors SET director_name = ? " +
                "WHERE director_id = ?";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        return findById(director.getId()).orElseThrow(() -> new DirectorNotFoundException(director.getId()));
    }

    @Override
    public void removeDirector(Integer directorId) {
        String sql = "DELETE FROM directors WHERE director_id = ?";
        jdbcTemplate.update(sql, directorId);
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT * FROM directors";
        return jdbcTemplate.query(sql,(rs, rn) -> DirectorConverter.fromResultSet(rs));
    }

    @Override
    public Optional<Director> findById(int directorId) {
        try {
            String sql = "SELECT * FROM directors WHERE director_id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rn) -> Optional.of(DirectorConverter.fromResultSet(rs)), directorId);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
