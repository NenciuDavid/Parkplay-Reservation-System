package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParkingLot {
    private Long id;
    private String name;
    private String location;
    private Long managerId;
    private List<ParkingSpot> spots = new ArrayList<>();

    public ParkingLot(String name, String location, Long managerId) {
        this.name = name;
        this.location = location;
        this.managerId = managerId;
    }

    public void addSpot(ParkingSpot spot) {
        this.spots.add(spot);
    }
}