package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindAllRoomdByHotelIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

class FindAllRoomsByHotelIdTest {

    private static class FakeFindAllRoomdByHotelIdOutputPort implements FindAllRoomdByHotelIdOutputPort {
        List<Room> toReturn = List.of();
        @Override
        public List<Room> findByHotelId(UUID hotelId) { return toReturn; }
    }

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        Optional<Hotel> toReturn = Optional.empty();
        @Override
        public Optional<Hotel> findHotelById(UUID id) { return toReturn; }
    }

    private FakeFindAllRoomdByHotelIdOutputPort findAllRoomdByHotelIdOutputPort;
    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FindAllRoomsByHotelId useCase;

    @BeforeEach
    void setUp() {
        findAllRoomdByHotelIdOutputPort = new FakeFindAllRoomdByHotelIdOutputPort();
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        useCase = new FindAllRoomsByHotelId(findAllRoomdByHotelIdOutputPort, findHotelByIdOutputPort);
    }

    @Test
    void handle_returnsRooms_whenHotelExists() throws Exception {
        UUID hotelId = UUID.randomUUID();
        findHotelByIdOutputPort.toReturn = Optional.of(Hotel.create("H", "A", new BigDecimal("10")));

        List<Room> rooms = List.of(Room.create("101", new BigDecimal("50"), 2));
        findAllRoomdByHotelIdOutputPort.toReturn = rooms;

        List<Room> result = useCase.handle(hotelId.toString());

        assertEquals(rooms, result);
    }

    @Test
    void handle_throwsNotFound_whenHotelMissing() {
        UUID hotelId = UUID.randomUUID();
        findHotelByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId.toString()));
    }
}
