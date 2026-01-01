package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**Resprezinta un loc de parcare asociat unei parcari*/
@Data
@NoArgsConstructor
public class ParkingSpot {
    private Long id;
    private String spotNumber;
    private boolean isAvailable;
    private double hourlyRate;
    private Long parkingLotId;

    public ParkingSpot(String spotNumber, double hourlyRate, boolean isAvailable) {
        this.spotNumber = spotNumber;
        this.hourlyRate = hourlyRate;
        this.isAvailable = isAvailable;
    }
}