package org.example.parkplayfinal.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    @Test
    void testNoArgsConstructor() {
        ParkingLot lot = new ParkingLot();
        assertNotNull(lot);
        assertNotNull(lot.getSpots(), "Lista de locuri ar trebui să fie inițializată goală, nu null");
    }

    @Test
    void testConstructorWithFields() {
        ParkingLot lot = new ParkingLot("Parcare Centru", "Timisoara", 5L);

        assertEquals("Parcare Centru", lot.getName());
        assertEquals("Timisoara", lot.getLocation());
        assertEquals(5L, lot.getManagerId());
    }

    @Test
    void testAddSpotFunctionality() {
        ParkingLot lot = new ParkingLot();
        lot.setSpots(new ArrayList<>());
        ParkingSpot spot = new ParkingSpot("A-1", 10.0, true);

        lot.addSpot(spot);

        assertEquals(1, lot.getSpots().size(), "Ar trebui să fie un loc în listă");
        assertEquals("A-1", lot.getSpots().get(0).getSpotNumber());
    }
}