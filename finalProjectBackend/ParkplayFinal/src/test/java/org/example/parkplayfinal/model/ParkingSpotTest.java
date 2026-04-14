package org.example.parkplayfinal.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {
    @Test
    void testNoArgsConstructor() {
        ParkingSpot spot = new ParkingSpot();
        assertNotNull(spot);
    }

    @Test
    void testConstructorWithFields() {
        ParkingSpot spot = new ParkingSpot("B-20", 15.5, true);

        assertEquals("B-20", spot.getSpotNumber());
        assertEquals(15.5, spot.getHourlyRate());
        assertTrue(spot.isAvailable(), "Locul ar trebui să fie marcat ca disponibil");
    }

    @Test
    void testAvailabilityToggle() {
        ParkingSpot spot = new ParkingSpot();
        spot.setAvailable(true);
        assertTrue(spot.isAvailable());

        spot.setAvailable(false);
        assertFalse(spot.isAvailable());
    }
}