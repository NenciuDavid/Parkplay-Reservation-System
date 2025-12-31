package org.example.parkplayfinal.service;

import org.example.parkplayfinal.exception.InvalidTimeSlotException;
import org.example.parkplayfinal.exception.SpotUnavailableException;
import org.example.parkplayfinal.model.User;
import org.example.parkplayfinal.model.ParkingSpot;
import org.example.parkplayfinal.repository.ParkingSpotRepository;
import org.example.parkplayfinal.repository.ReservationRepository;
import org.example.parkplayfinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ParkingSpotRepository spotRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Long driverId = 1L;
    private Long spotId = 10L;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createReservation_Success() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        when(userRepository.findById(driverId)).thenReturn(Optional.of(new User()));
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(new ParkingSpot()));
        when(reservationRepository.hasOverlaps(spotId, start, end)).thenReturn(false);

        reservationService.createReservation(driverId, spotId, start, end);

        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void createReservation_InvalidTime_ThrowsException() {
        LocalDateTime start = LocalDateTime.now().plusHours(5);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        assertThrows(InvalidTimeSlotException.class, () -> {
            reservationService.createReservation(driverId, spotId, start, end);
        });

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_SpotOccupied_ThrowsException() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        when(userRepository.findById(driverId)).thenReturn(Optional.of(new User()));
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(new ParkingSpot()));

        when(reservationRepository.hasOverlaps(spotId, start, end)).thenReturn(true);

        assertThrows(SpotUnavailableException.class, () -> {
            reservationService.createReservation(driverId, spotId, start, end);
        });

        verify(reservationRepository, never()).save(any());
    }
}