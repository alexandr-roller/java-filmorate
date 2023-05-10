package ru.yandex.practicum.filmorate.storage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component("DirectorDbStorage")
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director addDirector( Director director) {
        log.info("Received a request addDirector({})", director);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("DIRECTORS")
                .usingGeneratedKeyColumns("DIRECTOR_ID");
        director.setId(simpleJdbcInsert.executeAndReturnKey(director.toMap()).intValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        log.info("Received a request updateDirector({})", director);
        String sql = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? " +
                "WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        log.info("The director {} was updated", director);
        return getDirector(director.getId());
    }

    @Override
    public void removeDirector(Integer directorId) {
        log.info("Received a request removeDirector({})", directorId);
        String sql = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sql, directorId);
        log.info("The director {} was deleted", directorId);
    }

    @Override
    public Collection<Director> getDirectors() {
        log.info("Received a request getDirectors()");
        String sql = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sql, this::mapRowtoDirector);
    }

    @Override
    public Director getDirector(Integer directorId) {
        log.info("Received a request getDirector({})", directorId);
        String sql = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowtoDirector, directorId);
    }

    public Director mapRowtoDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("DIRECTOR_ID"))
                .name(resultSet.getString("DIRECTOR_NAME"))
                .build();
    }
}
