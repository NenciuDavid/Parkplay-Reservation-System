package org.example.parkplayfinal.repository;

import org.example.parkplayfinal.model.ParkingLot;
import org.example.parkplayfinal.model.ParkingSpot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParkingLotRepository {
    private final JdbcTemplate jdbcTemplate;

    public ParkingLotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ParkingLot> lotMapper = (rs, rowNum) -> {
        ParkingLot lot = new ParkingLot();
        lot.setId(rs.getLong("id"));
        lot.setName(rs.getString("name"));
        lot.setLocation(rs.getString("location"));
        lot.setManagerId(rs.getLong("manager_id"));
        return lot;
    };

    private final RowMapper<ParkingSpot> spotMapper = (rs, rowNum) -> {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(rs.getLong("id"));
        spot.setSpotNumber(rs.getString("spot_number"));
        spot.setAvailable(rs.getBoolean("is_available"));
        spot.setHourlyRate(rs.getDouble("hourly_rate"));
        spot.setParkingLotId(rs.getLong("parking_lot_id"));
        return spot;
    };

    public List<ParkingLot> findAllWithSpots() {
        List<ParkingLot> lots = jdbcTemplate.query("SELECT * FROM parking_lots", lotMapper);

        for (ParkingLot lot : lots) {
            String sqlSpots = "SELECT * FROM parking_spots WHERE parking_lot_id = ?";
            List<ParkingSpot> spots = jdbcTemplate.query(sqlSpots, spotMapper, lot.getId());
            lot.setSpots(spots);
        }
        return lots;
    }

    public void save(ParkingLot lot) {
        jdbcTemplate.update("INSERT INTO parking_lots (name, location, manager_id) VALUES (?, ?, ?)",
                lot.getName(), lot.getLocation(), lot.getManagerId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM parking_lots WHERE id = ?", id);
    }

    public int update(Long id, ParkingLot lot) {
        return jdbcTemplate.update("UPDATE parking_lots SET name = ?, location = ? WHERE id = ?",
                lot.getName(), lot.getLocation(), id);
    }

    public List<ParkingLot> findByManagerId(Long managerId) {
        String sql = "SELECT * FROM parking_lots WHERE manager_id = ?";
        List<ParkingLot> lots = jdbcTemplate.query(sql, lotMapper, managerId);
        for (ParkingLot lot : lots) {
            String sqlSpots = "SELECT * FROM parking_spots WHERE parking_lot_id = ?";
            lot.setSpots(jdbcTemplate.query(sqlSpots, spotMapper, lot.getId()));
        }
        return lots;
    }

    public Optional<ParkingLot> findById(Long id) {
        String sql = "SELECT * FROM parking_lots WHERE id = ?";
        return jdbcTemplate.query(sql, lotMapper, id).stream().findFirst();
    }
}