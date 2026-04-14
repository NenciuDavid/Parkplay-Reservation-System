package org.example.parkplayfinal.service;

import org.example.parkplayfinal.exception.InvalidTimeSlotException;
import org.example.parkplayfinal.exception.SpotUnavailableException;
import org.example.parkplayfinal.model.Reservation;
import org.example.parkplayfinal.repository.ParkingSpotRepository;
import org.example.parkplayfinal.repository.ReservationRepository;
import org.example.parkplayfinal.repository.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.example.parkplayfinal.ds.BinomialHeap;
import org.example.parkplayfinal.ds.RBTree;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Serviciul care se ocupa cu logica rezervarilor, validarea si verificarea eventualelor suprapuneri*/
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository spotRepository;
    private final UserRepository userRepository;

    private Map<Long, RBTree> spotTrees = new HashMap<>();
    private Map<Long, BinomialHeap> lotHeaps = new HashMap<>();

    /**constructor*/
    public ReservationService(ReservationRepository reservationRepository,
                              ParkingSpotRepository spotRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDataStructures() {
        List<Reservation> allReservations = reservationRepository.findAll();
        for (Reservation res : allReservations) {
            spotTrees.putIfAbsent(res.getSpotId(), new RBTree());
            spotTrees.get(res.getSpotId()).RBInsert(res);

            if (res.getEndTime().isAfter(LocalDateTime.now())) {
                spotRepository.findById(res.getSpotId()).ifPresent(spot -> {
                    Long lotId = spot.getLotId();
                    lotHeaps.putIfAbsent(lotId, new BinomialHeap());
                    lotHeaps.get(lotId).insert(res);
                });
            }
        }
        System.out.println("Structurile de date au fost încărcate cu succes!");
    }

    /**Functie responsabila cu creearea rezervarilor si validarea acesteia*/
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

        spotTrees.putIfAbsent(spotId, new RBTree());
        RBTree tree = spotTrees.get(spotId);

        if (tree.hasOverlap(start, end)) {
            throw new SpotUnavailableException("Locul " + spotId + " este deja rezervat în acest interval!");
        }

        Reservation reservation = new Reservation(driverId, spotId, start, end);
        reservationRepository.save(reservation);

        tree.RBInsert(reservation);
        spotRepository.findById(spotId).ifPresent(spot -> {
            Long lotId = spot.getLotId();
            lotHeaps.putIfAbsent(lotId, new BinomialHeap());
            lotHeaps.get(lotId).insert(reservation);
        });
    }

    public Reservation getNextExpiringReservation(Long lotId) {
        BinomialHeap heap = lotHeaps.get(lotId);
        if (heap == null || heap.minimum() == null) {
            return null;
        }
        return heap.minimum().data;
    }

    public boolean deleteReservation(Long id) {
        int deletedRows = reservationRepository.deleteById(id);

        if (deletedRows > 0) {
            spotTrees.clear();
            lotHeaps.clear();

            initDataStructures();
            return true;
        }
        return false;
    }
}