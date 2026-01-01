package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**Reprezinta o parcare care contine locuri de parcare, are id, nume si locatie si este asociata unui user cu rolul de parking manager*/
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