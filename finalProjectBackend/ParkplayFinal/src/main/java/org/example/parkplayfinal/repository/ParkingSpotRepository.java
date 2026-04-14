package org.example.parkplayfinal.repository;

import org.example.parkplayfinal.model.ParkingSpot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**Repository responsabil cu gestionarea locurilor de parcare (tabela parking_spots)*/
@Repository
public class ParkingSpotRepository {

    private final JdbcTemplate jdbcTemplate;

    /**Constructor*/
    public ParkingSpotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**Mapper pentru locurile de parcare*/
    public static final RowMapper<ParkingSpot> spotMapper = (rs, rowNum) -> {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(rs.getLong("id"));
        spot.setSpotNumber(rs.getString("spot_number"));
        spot.setAvailable(rs.getBoolean("is_available"));
        spot.setHourlyRate(rs.getDouble("hourly_rate"));
        spot.setParkingLotId(rs.getLong("parking_lot_id"));
        return spot;
    };

    /**Functie care gaseste un loc dupa id-ul acestuia in baza de date*/
    public Optional<ParkingSpot> findById(Long id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";
        return jdbcTemplate.query(sql, spotMapper, id).stream().findFirst();
    }

    /**Functie care gaseste in baza de date un loc de parcare dupa id-ul parcarii de care apartine*/
    public List<ParkingSpot> findByParkingLotId(Long parkingLotId) {
        String sql = "SELECT * FROM parking_spots WHERE parking_lot_id = ?";
        return jdbcTemplate.query(sql, spotMapper, parkingLotId);
    }

    /**Functie responsabila cu salvarea in baza de date a unui loc de parcare*/
    public int save(ParkingSpot spot) {
        String sql = "INSERT INTO parking_spots (spot_number, is_available, hourly_rate, parking_lot_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                spot.getSpotNumber(),
                spot.isAvailable(),
                spot.getHourlyRate(),
                spot.getParkingLotId());
    }

    /**Functie resposabia cu stergerea unui loc de parcare din baza de date*/
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM parking_spots WHERE id = ?", id);
    }
}