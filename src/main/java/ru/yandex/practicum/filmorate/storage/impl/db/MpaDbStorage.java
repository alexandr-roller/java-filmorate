package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.converter.MpaConverter;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("select * from mpa order by mpa_id", (rs, rn) -> MpaConverter.fromResultSet(rs));
    }

    @Override
    public Optional<Mpa> findById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from mpa where mpa_id = ?",
                    (rs, rn) -> Optional.of(MpaConverter.fromResultSet(rs)),
                    id);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
