package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.CreateRoomDTO;
import com.sa.establishment_service.hotels.application.outputports.CreateRoomOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

class CreateRoomUseCaseTest {

    private static class FakeCreateRoomOutputPort implements CreateRoomOutputPort {
        String lastHotelId;
        Room lastRoom;
        Room toReturn;
        @Override
        public Room createRoom(String hotelId, Room room) {
            this.lastHotelId = hotelId;
            this.lastRoom = room;
            return toReturn != null ? toReturn : room;
        }
    }

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        Optional<Hotel> toReturn = Optional.empty();
        @Override
        public Optional<Hotel> findHotelById(UUID id) {
            return toReturn;
        }
    }

    private FakeCreateRoomOutputPort createRoomOutputPort;
    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private CreateRoomUseCase useCase;

    @BeforeEach
    void setup() {
        createRoomOutputPort = new FakeCreateRoomOutputPort();
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        useCase = new CreateRoomUseCase(createRoomOutputPort, findHotelByIdOutputPort);
    }

    private CreateRoomDTO buildDTO(UUID hotelId, String number) {
        return new CreateRoomDTO(number, new BigDecimal("99.99"), 2, hotelId);
    }

    @Test
    void handle_createsRoom_whenHotelExistsAndNumberNotDuplicated() throws Exception {
        UUID hotelId = UUID.randomUUID();
        CreateRoomDTO dto = buildDTO(hotelId, "101");

        Hotel hotel = Hotel.create("H1", "Addr", new BigDecimal("100"));
        hotel.setRooms(new ArrayList<>());
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);

        Room persisted = Room.create(dto.getNumber(), dto.getPricePerNight(), dto.getCapacity());
        createRoomOutputPort.toReturn = persisted;

        Room result = useCase.handle(dto);

        assertEquals(persisted, result);
        assertEquals(hotelId.toString(), createRoomOutputPort.lastHotelId);
        assertEquals("101", createRoomOutputPort.lastRoom.getNumber());
    }

    @Test
    void handle_throwsNotFound_whenHotelDoesNotExist() {
        UUID hotelId = UUID.randomUUID();
        CreateRoomDTO dto = buildDTO(hotelId, "101");

        findHotelByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_throwsDuplicatedEntry_whenRoomNumberAlreadyExists() {
        UUID hotelId = UUID.randomUUID();
        CreateRoomDTO dto = buildDTO(hotelId, "101");

        Hotel hotel = Hotel.create("H1", "Addr", new BigDecimal("100"));
        List<Room> rooms = new ArrayList<>();
        rooms.add(Room.create("101", new BigDecimal("50.00"), 2));
        hotel.setRooms(rooms);
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);

        assertThrows(DuplicatedEntryException.class, () -> useCase.handle(dto));
    }
}
