package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("Получень запрос getAllMpa");
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpa(Integer mpaId) {
        try {
            log.info("Получень запрос getMpa");
            String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, mpaId);
        } catch (Exception e) {
            log.error("MPA c id {} не найден", mpaId);
            throw new MpaNotFoundException(String.format("MPA с id %s не найден", mpaId));
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
