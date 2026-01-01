package org.example.parkplayfinal.controller;

import org.example.parkplayfinal.model.Reservation;
import org.example.parkplayfinal.repository.ReservationRepository;
import org.example.parkplayfinal.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**Controller responsabil cu gestionarea enpoint-urilor legate de rezervari*/
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    /**constructor*/
    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    /**Functie responsabila cu creearea unei rezervari*/
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

    /**Functie responsabila cu afisarea informatilor despre o parcare*/
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**Functie respnsabila cu stergerea unei rezervari*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        if (reservationRepository.deleteById(id) > 0) {
            return ResponseEntity.ok("Rezervare anulată!");
        }
        return ResponseEntity.notFound().build();
    }

    /**Functie responsabila cu afisarea rezervarilor unui sofer*/
    @GetMapping("/driver/{driverId}")
    public List<Reservation> getReservationsByDriver(@PathVariable Long driverId) {
        return reservationRepository.findByDriverId(driverId);
    }
}