package com.sa.client_service.reservations.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.outputports.CheckRoomAvailabilityOutputPort;
import com.sa.client_service.reservations.application.outputports.FindAllRoomsOutputPort;

@ExtendWith(MockitoExtension.class)
public class FindAvailableRoomsUseCaseTest {

    @Mock private FindAllRoomsOutputPort findAllRoomsOutputPort;
    @Mock private CheckRoomAvailabilityOutputPort checkRoomAvailabilityOutputPort;

    private FindAvailableRoomsUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new FindAvailableRoomsUseCase(findAllRoomsOutputPort, checkRoomAvailabilityOutputPort);
    }

    @Test
    void givenNoHotelFilter_returnsOnlyAvailableRooms() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);
        String hotel1 = UUID.randomUUID().toString();
        String hotel2 = UUID.randomUUID().toString();

        RoomDTO r1 = new RoomDTO(UUID.randomUUID().toString(), "101", null, 2, hotel1, "H1");
        RoomDTO r2 = new RoomDTO(UUID.randomUUID().toString(), "102", null, 2, hotel2, "H2");

        when(findAllRoomsOutputPort.findAll()).thenReturn(List.of(r1, r2));
        when(checkRoomAvailabilityOutputPort.isAvailable(UUID.fromString(hotel1), UUID.fromString(r1.getId()), start, end))
            .thenReturn(true);
        when(checkRoomAvailabilityOutputPort.isAvailable(UUID.fromString(hotel2), UUID.fromString(r2.getId()), start, end))
            .thenReturn(false);

        List<RoomDTO> result = useCase.handle(null, start, end);

        assertEquals(1, result.size());
        assertEquals(r1.getId(), result.get(0).getId());
        verify(findAllRoomsOutputPort, times(1)).findAll();
    }

    @Test
    void givenHotelFilter_returnsRoomsMatchingHotelId_currentImplementationIgnoresAvailability() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);
        String hotel1 = UUID.randomUUID().toString();
        String hotel2 = UUID.randomUUID().toString();

        RoomDTO r1 = new RoomDTO(UUID.randomUUID().toString(), "201", null, 2, hotel1, "H1");
        RoomDTO r2 = new RoomDTO(UUID.randomUUID().toString(), "202", null, 2, hotel2, "H2");

        when(findAllRoomsOutputPort.findAll()).thenReturn(List.of(r1, r2));
        // Even if availability says false for r1, current code replaces availableRooms from allRooms by hotel filter
        when(checkRoomAvailabilityOutputPort.isAvailable(UUID.fromString(hotel1), UUID.fromString(r1.getId()), start, end))
            .thenReturn(false);
        when(checkRoomAvailabilityOutputPort.isAvailable(UUID.fromString(hotel2), UUID.fromString(r2.getId()), start, end))
            .thenReturn(true);

        List<RoomDTO> result = useCase.handle(hotel1, start, end);

        assertEquals(1, result.size());
        assertEquals(r1.getId(), result.get(0).getId());
    }
}

