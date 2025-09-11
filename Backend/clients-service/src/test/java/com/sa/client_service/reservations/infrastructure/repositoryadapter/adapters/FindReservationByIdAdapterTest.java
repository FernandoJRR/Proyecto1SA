package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class FindReservationByIdAdapterTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationRepositoryMapper reservationRepositoryMapper;

    private FindReservationByIdAdapter adapter;

    private static final String RES_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setup() {
        adapter = new FindReservationByIdAdapter(reservationRepository, reservationRepositoryMapper);
    }

    @Test
    void givenExistingReservation_whenFindById_thenReturnsDomainOptional() {
        ReservationEntity entity = new ReservationEntity();
        when(reservationRepository.findById(RES_ID)).thenReturn(Optional.of(entity));
        Reservation domain = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        Optional<Reservation> result = adapter.findById(RES_ID);

        assertTrue(result.isPresent());
        verify(reservationRepository, times(1)).findById(RES_ID);
        verify(reservationRepositoryMapper, times(1)).toDomain(same(entity));
    }

    @Test
    void givenMissingReservation_whenFindById_thenReturnsEmptyAndNoMap() {
        when(reservationRepository.findById(RES_ID)).thenReturn(Optional.empty());

        Optional<Reservation> result = adapter.findById(RES_ID);

        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findById(RES_ID);
        verify(reservationRepositoryMapper, never()).toDomain(any(ReservationEntity.class));
    }
}

