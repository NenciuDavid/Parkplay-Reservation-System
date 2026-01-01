package org.example.parkplayfinal.service;

import org.example.parkplayfinal.exception.InvalidTimeSlotException;
import org.example.parkplayfinal.exception.SpotUnavailableException;
import org.example.parkplayfinal.model.Reservation;
import org.example.parkplayfinal.repository.ParkingSpotRepository;
import org.example.parkplayfinal.repository.ReservationRepository;
import org.example.parkplayfinal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
/**Serviciul care se ocupa cu logica rezervarilor, validarea si verificarea eventualelor suprapuneri*/
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository spotRepository;
    private final UserRepository userRepository;

    /**constructor*/
    public ReservationService(ReservationRepository reservationRepository,
                              ParkingSpotRepository spotRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
    }

    /**Functie responsabila cu creearea rezervarilor si validarea acesteia dupa urmatoarele reguli: validarea intervalului orar, verificarea existentei locului si a soferului si verificarea eventualelor suprapuneri*/
    public void createReservation(Long driverId, Long spotId, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new InvalidTimeSlotException("Interval invalid: Ora de start trebuie să fie înaintea orei de final.");
        }

        if (userRepository.findById(driverId).isEmpty()) {
            throw new RuntimeException("Șoferul cu id " + driverId + " nu există.");
        }
        if (spotRepository.findById(spotId).isEmpty()) {
            throw new RuntimeException("Locul de parcare cu id " + spotId + " nu există.");
        }

        if (reservationRepository.hasOverlaps(spotId, start, end)) {
            throw new SpotUnavailableException("Locul " + spotId + " este deja rezervat în acest interval!");
        }

        Reservation reservation = new Reservation(driverId, spotId, start, end);
        reservationRepository.save(reservation);
    }
}