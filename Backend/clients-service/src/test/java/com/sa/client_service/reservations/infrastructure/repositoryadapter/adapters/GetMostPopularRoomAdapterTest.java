package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class GetMostPopularRoomAdapterTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationRepositoryMapper reservationRepositoryMapper;

    private GetMostPopularRoomAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new GetMostPopularRoomAdapter(reservationRepository, reservationRepositoryMapper);
    }

    @Test
    void givenHotelId_whenNoResults_thenReturnsEmpty() {
        String hotelId = UUID.randomUUID().toString();
        when(reservationRepository.findTopRoomsByHotel(any(UUID.class), any(Pageable.class)))
            .thenReturn(Collections.emptyList());

        Optional<Reservation> result = adapter.getMostPopular(hotelId);

        assertFalse(result.isPresent());
        verify(reservationRepository, times(1))
            .findTopRoomsByHotel(any(UUID.class), any(Pageable.class));
    }

    @Test
    void givenHotelId_whenResults_thenMapsFirstAndReturns() {
        String hotelId = UUID.randomUUID().toString();
        ReservationEntity entity = new ReservationEntity();
        when(reservationRepository.findTopRoomsByHotel(any(UUID.class), any(Pageable.class)))
            .thenReturn(List.of(entity));
        Reservation mapped = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(entity)).thenReturn(mapped);

        Optional<Reservation> result = adapter.getMostPopular(hotelId);

        assertTrue(result.isPresent());
        assertSame(mapped, result.get());
        verify(reservationRepositoryMapper, times(1)).toDomain(entity);
    }

    @Test
    void givenNullHotelId_whenNoResults_thenReturnsEmpty() {
        when(reservationRepository.findTopRoomsGlobally(any(Pageable.class)))
            .thenReturn(Collections.emptyList());

        Optional<Reservation> result = adapter.getMostPopular(null);

        assertFalse(result.isPresent());
        verify(reservationRepository, times(1))
            .findTopRoomsGlobally(any(Pageable.class));
    }

    @Test
    void givenNullHotelId_whenResults_thenMapsFirstAndReturns() {
        ReservationEntity entity = new ReservationEntity();
        when(reservationRepository.findTopRoomsGlobally(any(Pageable.class)))
            .thenReturn(List.of(entity));
        Reservation mapped = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(entity)).thenReturn(mapped);

        Optional<Reservation> result = adapter.getMostPopular(null);

        assertTrue(result.isPresent());
        assertSame(mapped, result.get());
        verify(reservationRepositoryMapper, times(1)).toDomain(entity);
    }
}

