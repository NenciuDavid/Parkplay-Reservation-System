package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**Resprezinta o rezervare a unui loc de parcare dintr-o parcare pentru un user cu rol de driver*/
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