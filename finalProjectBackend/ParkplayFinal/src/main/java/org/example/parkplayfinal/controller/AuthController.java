package org.example.parkplayfinal.controller;

import org.example.parkplayfinal.model.User;
import org.example.parkplayfinal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
/**
 * Controller responsabil pentru autentificarea și înregistrarea utilizatorilor.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    /** Constructor*/
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Functie responsabila cu inregistrarea noilor utilizatori*/
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email deja folosit.");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User înregistrat cu succes!");
    }

    /** Functie responsabila cu logarea userilor existenti*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email sau parolă greșită.");
    }
}