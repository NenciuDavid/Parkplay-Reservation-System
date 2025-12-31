package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Reservation {
    private Long id;
    private Long driverId;
    private Long spotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation(Long driverId, Long spotId, LocalDateTime startTime, LocalDateTime endTime) {
        this.driverId = driverId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}