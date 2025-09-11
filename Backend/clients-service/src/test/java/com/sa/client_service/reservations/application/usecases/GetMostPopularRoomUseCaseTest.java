package com.sa.client_service.reservations.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.MostPopularRoomDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.outputports.FindHotelByIdOutputPort;
import com.sa.client_service.reservations.application.outputports.FindReservationsOutputPort;
import com.sa.client_service.reservations.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.client_service.reservations.application.outputports.GetMostPopularRoomOutputPort;
import com.sa.shared.exceptions.NotFoundException;
import com.sa.client_service.reservations.domain.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GetMostPopularRoomUseCaseTest {

    @Mock
    private GetMostPopularRoomOutputPort getMostPopularRoomOutputPort;

    @Mock
    private FindReservationsOutputPort findReservationsOutputPort;

    @Mock
    private FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;

    @Mock
    private FindHotelByIdOutputPort findHotelByIdOutputPort;

    private GetMostPopularRoomUseCase useCase;

    private final String hotelId = UUID.randomUUID().toString();
    private final String roomId = UUID.randomUUID().toString();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetMostPopularRoomUseCase(
            getMostPopularRoomOutputPort,
            findReservationsOutputPort,
            findRoomByHotelAndIdOutputPort,
            findHotelByIdOutputPort
        );
    }

    @Test
    void givenExistingMostPopular_whenHandle_thenReturnDTO() throws Exception {
        // Arrange
        // Create a fake reservation
        Reservation res = new Reservation(
            UUID.randomUUID(),
            /* client */ null,
            UUID.fromString(hotelId),
            UUID.fromString(roomId),
            /* startDate */ null,
            /* endDate */ null,
            /* promotionApplied */ null,
            /* subtotal */ null,
            /* totalCost */ null,
            /* status */ null
        );

        // stub getMostPopular
        when(getMostPopularRoomOutputPort.getMostPopular(hotelId))
            .thenReturn(Optional.of(res));

        // stub hotel lookup
        HotelDTO hotelDTO = new HotelDTO(hotelId, "Hotel X", "Address", null);
        when(findHotelByIdOutputPort.findById(hotelId))
            .thenReturn(Optional.of(hotelDTO));

        // stub reservations listing
        List<Reservation> dummyList = List.of(res);
        when(findReservationsOutputPort.findReservations(null, UUID.fromString(roomId), null, null, null))
            .thenReturn(dummyList);

        // stub room lookup
        RoomDTO roomDTO = new RoomDTO(roomId, "101", null, 2, hotelId, "Hotel X");
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId, roomId))
            .thenReturn(Optional.of(roomDTO));

        // Act
        MostPopularRoomDTO result = useCase.handle(hotelId);

        // Assert
        assertNotNull(result);
        assert result.getHotelName().equals("Hotel X");
        assert result.getRoomNumber().equals("101");
        assert result.getReservations() == dummyList;

        verify(getMostPopularRoomOutputPort).getMostPopular(hotelId);
        verify(findHotelByIdOutputPort).findById(hotelId);
        verify(findReservationsOutputPort).findReservations(null, UUID.fromString(roomId), null, null, null);
        verify(findRoomByHotelAndIdOutputPort).findByHotelAndId(hotelId, roomId);
    }

    @Test
    void givenNoPopularRoom_whenHandle_thenThrowsNotFound() {
        // stub empty getMostPopular
        when(getMostPopularRoomOutputPort.getMostPopular(hotelId))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId));
    }

    @Test
    void givenHotelNotFound_whenHandle_thenThrowsNotFound() {
        // Arrange: getMostPopular returns reservation
        Reservation res = new Reservation(
            UUID.randomUUID(), null,
            UUID.fromString(hotelId), UUID.fromString(roomId),
            null, null, null, null, null, null
        );
        when(getMostPopularRoomOutputPort.getMostPopular(hotelId))
            .thenReturn(Optional.of(res));

        // stub hotel lookup empty
        when(findHotelByIdOutputPort.findById(hotelId))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId));
    }

    @Test
    void givenRoomNotFound_whenHandle_thenThrowsNotFound() {
        // Arrange
        Reservation res = new Reservation(
            UUID.randomUUID(), null,
            UUID.fromString(hotelId), UUID.fromString(roomId),
            null, null, null, null, null, null
        );
        when(getMostPopularRoomOutputPort.getMostPopular(hotelId))
            .thenReturn(Optional.of(res));

        HotelDTO hotelDTO = new HotelDTO(hotelId, "Hotel Y", "Addr", null);
        when(findHotelByIdOutputPort.findById(hotelId))
            .thenReturn(Optional.of(hotelDTO));

        when(findReservationsOutputPort.findReservations(null, UUID.fromString(roomId), null, null, null))
            .thenReturn(List.of()); // maybe not crucial

        // stub room empty
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId, roomId))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId));
    }
}