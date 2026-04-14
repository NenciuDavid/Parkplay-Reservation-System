package org.example.parkplayfinal.controller;

import org.example.parkplayfinal.model.User;
import org.example.parkplayfinal.model.ParkingLot;
import org.example.parkplayfinal.repository.UserRepository;
import org.example.parkplayfinal.repository.ParkingLotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**Controller responsabil cu gestionarea enpoint-urilor legate de user*/
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;

    /**constructor*/
    public UserController(UserRepository userRepository, ParkingLotRepository parkingLotRepository) {
        this.userRepository = userRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    /**Functie respnsabila cu afisarea profilului unui user*/
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**Functie responsabila cu afisarea parcarilor unui parking manager*/
    @GetMapping("/managers/{id}/lots")
    public List<ParkingLot> getManagerLots(@PathVariable Long id) {
        return parkingLotRepository.findByManagerId(id);
    }
}