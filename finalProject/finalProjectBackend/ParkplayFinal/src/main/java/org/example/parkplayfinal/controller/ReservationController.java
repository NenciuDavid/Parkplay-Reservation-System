package org.example.parkplayfinal.controller;

import org.example.parkplayfinal.model.Reservation;
import org.example.parkplayfinal.repository.ReservationRepository;
import org.example.parkplayfinal.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    
    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation req) {

        reservationService.createReservation(
                req.getDriverId(),
                req.getSpotId(),
                req.getStartTime(),
                req.getEndTime()
        );

        return ResponseEntity.ok("Rezervare efectuată cu succes!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        if (reservationRepository.deleteById(id) > 0) {
            return ResponseEntity.ok("Rezervare anulată!");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/{driverId}")
    public List<Reservation> getReservationsByDriver(@PathVariable Long driverId) {
        return reservationRepository.findByDriverId(driverId);
    }
}