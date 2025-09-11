package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class CheckRoomAvailabilityAdapterTest {

    @Mock private ReservationRepository reservationRepository;

    private CheckRoomAvailabilityAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new CheckRoomAvailabilityAdapter(reservationRepository);
    }

    @Test
    void whenOverlapExists_thenIsAvailableReturnsFalse() {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);

        when(reservationRepository.existsOverlappingReservation(hotelId, roomId, start, end))
            .thenReturn(true);

        boolean available = adapter.isAvailable(hotelId, roomId, start, end);

        assertFalse(available);
        verify(reservationRepository, times(1))
            .existsOverlappingReservation(hotelId, roomId, start, end);
    }

    @Test
    void whenNoOverlap_thenIsAvailableReturnsTrue() {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);

        when(reservationRepository.existsOverlappingReservation(hotelId, roomId, start, end))
            .thenReturn(false);

        boolean available = adapter.isAvailable(hotelId, roomId, start, end);

        assertTrue(available);
        verify(reservationRepository, times(1))
            .existsOverlappingReservation(hotelId, roomId, start, end);
    }
}

