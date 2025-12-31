package org.example.parkplayfinal.repository;

import org.example.parkplayfinal.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasOverlaps(Long spotId, LocalDateTime start, LocalDateTime end) {
        String sql = """
            SELECT COUNT(*) FROM reservations 
            WHERE spot_id = ? 
            AND ( (start_time < ? AND end_time > ?) )
        """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, spotId, end, start);
        return count != null && count > 0;
    }

    public void save(Reservation r) {
        jdbcTemplate.update(
                "INSERT INTO reservations (driver_id, spot_id, start_time, end_time) VALUES (?, ?, ?, ?)",
                r.getDriverId(), r.getSpotId(), r.getStartTime(), r.getEndTime()
        );
    }

    private final RowMapper<Reservation> resMapper = (rs, rowNum) -> {
        Reservation r = new Reservation();
        r.setId(rs.getLong("id"));
        r.setDriverId(rs.getLong("driver_id"));
        r.setSpotId(rs.getLong("spot_id"));
        r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        return r;
    };

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        return jdbcTemplate.query(sql, resMapper, id).stream().findFirst();
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservations WHERE id = ?", id);
    }

    public List<Reservation> findByDriverId(Long driverId) {
        String sql = "SELECT * FROM reservations WHERE driver_id = ?";
        return jdbcTemplate.query(sql, resMapper, driverId);
    }
}