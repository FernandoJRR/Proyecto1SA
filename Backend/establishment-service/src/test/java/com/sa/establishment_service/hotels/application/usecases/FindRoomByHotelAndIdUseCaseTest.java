package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

class FindRoomByHotelAndIdUseCaseTest {

    private static class FakeFindRoomByHotelAndIdOutputPort implements FindRoomByHotelAndIdOutputPort {
        String expectedHotelId;
        String expectedRoomId;
        Optional<Room> toReturn = Optional.empty();
        @Override
        public Optional<Room> findByHotelAndId(String hotelId, String roomId) {
            if ((expectedHotelId == null || expectedHotelId.equals(hotelId)) &&
                (expectedRoomId == null || expectedRoomId.equals(roomId))) {
                return toReturn;
            }
            return Optional.empty();
        }
    }

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        Optional<Hotel> toReturn = Optional.empty();
        @Override
        public Optional<Hotel> findHotelById(UUID id) { return toReturn; }
    }

    private FakeFindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FindRoomByHotelAndIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findRoomByHotelAndIdOutputPort = new FakeFindRoomByHotelAndIdOutputPort();
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        useCase = new FindRoomByHotelAndIdUseCase(findRoomByHotelAndIdOutputPort, findHotelByIdOutputPort);
    }

    @Test
    void handle_returnsRoom_whenHotelAndRoomExist() throws Exception {
        String hotelId = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();

        findHotelByIdOutputPort.toReturn = Optional.of(Hotel.create("H", "A", new BigDecimal("10")));

        Room room = Room.create("101", new BigDecimal("50"), 2);
        findRoomByHotelAndIdOutputPort.expectedHotelId = hotelId;
        findRoomByHotelAndIdOutputPort.expectedRoomId = roomId;
        findRoomByHotelAndIdOutputPort.toReturn = Optional.of(room);

        Room result = useCase.handle(hotelId, roomId);

        assertEquals(room, result);
    }

    @Test
    void handle_throwsNotFound_whenHotelMissing() {
        String hotelId = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();
        findHotelByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId, roomId));
    }

    @Test
    void handle_throwsNotFound_whenRoomMissing() {
        String hotelId = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();

        findHotelByIdOutputPort.toReturn = Optional.of(Hotel.create("H", "A", new BigDecimal("10")));
        findRoomByHotelAndIdOutputPort.expectedHotelId = hotelId;
        findRoomByHotelAndIdOutputPort.expectedRoomId = roomId;
        findRoomByHotelAndIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId, roomId));
    }
}
