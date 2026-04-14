package org.example.parkplayfinal.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testNoArgsConstructor() {
        Reservation res = new Reservation();
        assertNotNull(res);
    }

    @Test
    void testConstructorWithFields() {
        Long driverId = 1L;
        Long spotId = 100L;
        LocalDateTime start = LocalDateTime.of(2025, 12, 30, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 30, 12, 0);

        Reservation res = new Reservation(driverId, spotId, start, end);

        assertEquals(driverId, res.getDriverId());
        assertEquals(spotId, res.getSpotId());
        assertEquals(start, res.getStartTime());
        assertEquals(end, res.getEndTime());
    }

    // Funcționalitate simplă: verificăm setarea ID-ului
    @Test
    void testSetId() {
        Reservation res = new Reservation();
        res.setId(55L);
        assertEquals(55L, res.getId());
    }
}