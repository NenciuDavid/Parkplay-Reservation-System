package org.example.parkplayfinal.controller;

import org.example.parkplayfinal.model.ParkingLot;
import org.example.parkplayfinal.model.ParkingSpot;
import org.example.parkplayfinal.repository.ParkingLotRepository;
import org.example.parkplayfinal.repository.ParkingSpotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**Controller responsabil cu gestionarea enpoint-urilor legate de parcari.*/
@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController {
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSpotRepository spotRepository;

    /**Constructor*/
    public ParkingLotController(ParkingLotRepository parkingLotRepository, ParkingSpotRepository spotRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.spotRepository = spotRepository;
    }

    /**Functia care ia toate parcarile*/
    @GetMapping
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAllWithSpots();
    }

    /**Functia responsabila cu creearea unei parcari noi*/
    @PostMapping
    public ResponseEntity<?> createParkingLot(@RequestBody ParkingLot lot) {
        parkingLotRepository.save(lot);
        return ResponseEntity.ok("Parcare creată!");
    }

    /**Functia responsabila cu adaugarea unui loc de parcare intr-o parcare*/
    @PostMapping("/{lotId}/spots")
    public ResponseEntity<?> addSpot(@PathVariable Long lotId, @RequestBody ParkingSpot spot) {
        spot.setParkingLotId(lotId);
        spotRepository.save(spot);
        return ResponseEntity.ok("Loc adăugat!");
    }

    /**Functie responsabila cu modificarea proprietatilor unei parcari*/
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParkingLot(@PathVariable Long id, @RequestBody ParkingLot lot) {
        if (parkingLotRepository.update(id, lot) > 0) {
            return ResponseEntity.ok("Parcare actualizată!");
        }
        return ResponseEntity.notFound().build();
    }

    /**Functie responsabila cu stergerea unei parcari*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParkingLot(@PathVariable Long id) {
        if (parkingLotRepository.deleteById(id) > 0) {
            return ResponseEntity.ok("Parcare ștearsă!");
        }
        return ResponseEntity.notFound().build();
    }

    /**Functie responsabila cu preluarea tuturor locurilor unei parcari*/
    @GetMapping("/{id}/spots")
    public List<ParkingSpot> getSpotsForLot(@PathVariable Long id) {
        return spotRepository.findByParkingLotId(id);
    }

    /**Functie responsabila cu stergerea unui loc de parcare*/
    @DeleteMapping("/spots/{spotId}")
    public ResponseEntity<?> deleteSpot(@PathVariable Long spotId) {
        if (spotRepository.deleteById(spotId) > 0) {
            return ResponseEntity.ok("Loc șters!");
        }
        return ResponseEntity.notFound().build();
    }
}