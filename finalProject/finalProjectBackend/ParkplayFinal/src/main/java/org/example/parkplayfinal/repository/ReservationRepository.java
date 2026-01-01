package org.example.parkplayfinal.repository;

import org.example.parkplayfinal.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**Repository responsabil cu gestionarea rezervarilor (tabela reservations)*/
@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    /**constructor*/
    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**Functie responsabila cu verificarea in baza de date a unei eventuale surapuneri*/
    public boolean hasOverlaps(Long spotId, LocalDateTime start, LocalDateTime end) {
        String sql = """
            SELECT COUNT(*) FROM reservations 
            WHERE spot_id = ? 
            AND ( (start_time < ? AND end_time > ?) )
        """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, spotId, end, start);
        return count != null && count > 0;
    }

    /**Functie responsabila cu salvarea in baza de date a unei rezervari*/
    public void save(Reservation r) {
        jdbcTemplate.update(
                "INSERT INTO reservations (driver_id, spot_id, start_time, end_time) VALUES (?, ?, ?, ?)",
                r.getDriverId(), r.getSpotId(), r.getStartTime(), r.getEndTime()
        );
    }

    /**Functie responsabila cu maparea rezervarilor*/
    private final RowMapper<Reservation> resMapper = (rs, rowNum) -> {
        Reservation r = new Reservation();
        r.setId(rs.getLong("id"));
        r.setDriverId(rs.getLong("driver_id"));
        r.setSpotId(rs.getLong("spot_id"));
        r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        return r;
    };

    /**Functie responsabila cu gasirea unei rezervari in baza de date dupa id-ul ei*/
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        return jdbcTemplate.query(sql, resMapper, id).stream().findFirst();
    }

    /**Functie responsabila cu stergerea unei rezervari din baza de date*/
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservations WHERE id = ?", id);
    }

    /**Functie responsabila cu gasirea rezervarilor unui sofer din baza de date*/
    public List<Reservation> findByDriverId(Long driverId) {
        String sql = "SELECT * FROM reservations WHERE driver_id = ?";
        return jdbcTemplate.query(sql, resMapper, driverId);
    }
}