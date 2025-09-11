package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class FindReservationsAdapterTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationRepositoryMapper reservationRepositoryMapper;

    private FindReservationsAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindReservationsAdapter(reservationRepository, reservationRepositoryMapper);
    }

    @Test
    void givenFilters_whenFindReservations_thenReturnsMappedDomains() {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        String cui = "1234567890123";
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);

        ReservationEntity e1 = new ReservationEntity();

        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Arrays.asList(e1));

        Reservation d1 = org.mockito.Mockito.mock(Reservation.class);
        Reservation d2 = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(e1)).thenReturn(d1);

        List<Reservation> results = adapter.findReservations(hotelId, roomId, cui, start, end);

        assertEquals(1, results.size());
        assertTrue(results.contains(d1));
        verify(reservationRepository, times(1)).findAll(any(Specification.class));
        verify(reservationRepositoryMapper, times(1)).toDomain(e1);
    }

    @Test
    void givenNoMatches_whenFindReservations_thenReturnsEmptyList() {
        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Collections.emptyList());

        List<Reservation> results = adapter.findReservations(null, null, null, null, null);

        assertTrue(results.isEmpty());
        verify(reservationRepository, times(1)).findAll(any(Specification.class));
    }
}
