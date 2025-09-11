package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class CreateReservationAdapterTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationRepositoryMapper reservationRepositoryMapper;

    private CreateReservationAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new CreateReservationAdapter(reservationRepository, reservationRepositoryMapper);
    }

    @Test
    void givenReservation_whenCreate_thenMapsSavesAndReturnsDomain() {
        // given
        Reservation domain = org.mockito.Mockito.mock(Reservation.class);
        ReservationEntity entity = new ReservationEntity();
        when(reservationRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domain)))
            .thenReturn(entity);
        when(reservationRepository.save(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(entity);
        Reservation mappedBack = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(mappedBack);

        // when
        Reservation result = adapter.createReservation(domain);

        // then
        assertSame(mappedBack, result);
        verify(reservationRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domain));
        verify(reservationRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(reservationRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }
}

