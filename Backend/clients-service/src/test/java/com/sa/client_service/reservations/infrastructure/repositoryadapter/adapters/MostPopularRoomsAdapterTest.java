package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class MostPopularRoomsAdapterTest {

    @Mock private ReservationRepository reservationRepository;

    private MostPopularRoomsAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new MostPopularRoomsAdapter(reservationRepository);
    }

    @Test
    void givenHotelId_whenQuery_thenDelegatesAndReturnsList() {
        UUID hotelId = UUID.randomUUID();
        List<UUID> expected = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(reservationRepository.findTopRoomsByHotel(hotelId)).thenReturn(expected);

        List<UUID> result = adapter.mostPopularRooms(hotelId);

        assertEquals(expected, result);
        verify(reservationRepository, times(1)).findTopRoomsByHotel(hotelId);
    }
}

