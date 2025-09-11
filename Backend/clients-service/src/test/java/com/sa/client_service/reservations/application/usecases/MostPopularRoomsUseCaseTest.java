package com.sa.client_service.reservations.application.usecases;

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

import com.sa.client_service.reservations.application.outputports.MostPopularRoomsOutputPort;

@ExtendWith(MockitoExtension.class)
public class MostPopularRoomsUseCaseTest {

    @Mock private MostPopularRoomsOutputPort mostPopularRoomsOutputPort;

    private MostPopularRoomsUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new MostPopularRoomsUseCase(mostPopularRoomsOutputPort);
    }

    @Test
    void delegatesToOutputPort_andReturnsRooms() {
        UUID hotelId = UUID.randomUUID();
        List<UUID> expected = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(mostPopularRoomsOutputPort.mostPopularRooms(hotelId)).thenReturn(expected);

        List<UUID> result = useCase.handle(hotelId);

        assertEquals(expected, result);
        verify(mostPopularRoomsOutputPort, times(1)).mostPopularRooms(hotelId);
    }
}

