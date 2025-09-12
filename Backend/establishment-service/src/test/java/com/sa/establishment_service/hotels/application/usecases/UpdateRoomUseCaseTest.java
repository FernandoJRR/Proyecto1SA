package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.UpdateRoomDTO;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.UpdateRoomOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.domain.RoomStatusEnum;
import com.sa.shared.exceptions.NotFoundException;
import com.sa.shared.exceptions.DuplicatedEntryException;

class UpdateRoomUseCaseTest {

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        UUID expectedId;
        UUID lastCalledId;
        Optional<Hotel> toReturn = Optional.empty();

        @Override
        public Optional<Hotel> findHotelById(UUID id) {
            lastCalledId = id;
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeFindRoomByHotelAndIdOutputPort implements FindRoomByHotelAndIdOutputPort {
        String expectedHotelId;
        String expectedRoomId;
        String lastHotelId;
        String lastRoomId;
        Optional<Room> toReturn = Optional.empty();

        @Override
        public Optional<Room> findByHotelAndId(String hotelId, String roomId) {
            lastHotelId = hotelId;
            lastRoomId = roomId;
            if ((expectedHotelId == null || expectedHotelId.equals(hotelId))
                    && (expectedRoomId == null || expectedRoomId.equals(roomId))) {
                return toReturn;
            }
            return Optional.empty();
        }
    }

    private static class FakeUpdateRoomOutputPort implements UpdateRoomOutputPort {
        Room lastArg;
        Room toReturn;

        @Override
        public Room updateRoom(Room room) {
            lastArg = room;
            return toReturn != null ? toReturn : room;
        }
    }

    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FakeFindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private FakeUpdateRoomOutputPort updateRoomOutputPort;
    private UpdateRoomUseCase useCase;

    @BeforeEach
    void setUp() {
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        findRoomByHotelAndIdOutputPort = new FakeFindRoomByHotelAndIdOutputPort();
        updateRoomOutputPort = new FakeUpdateRoomOutputPort();
        useCase = new UpdateRoomUseCase(findHotelByIdOutputPort, findRoomByHotelAndIdOutputPort, updateRoomOutputPort);
    }

    @Test
    void handle_updatesFields_andDelegatesToOutputPort() throws Exception {
        UUID hotelId = UUID.randomUUID();
        String roomId = UUID.randomUUID().toString();

        // Hotel exists
        findHotelByIdOutputPort.expectedId = hotelId;

        // Existing room
        Room existing = new Room(UUID.fromString(roomId), "101", new BigDecimal("50.00"), 2, RoomStatusEnum.AVAILABLE);
        Hotel hotel = Hotel.create("H", "A", new BigDecimal("10.00"));
        // Ensure rooms list is not null for duplicate check
        hotel.setRooms(java.util.Arrays.asList(existing));
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);
        findRoomByHotelAndIdOutputPort.expectedHotelId = hotelId.toString();
        findRoomByHotelAndIdOutputPort.expectedRoomId = roomId;
        findRoomByHotelAndIdOutputPort.toReturn = Optional.of(existing);

        // Update payload
        UpdateRoomDTO dto = new UpdateRoomDTO();
        dto.setNumber("102");
        dto.setPricePerNight(new BigDecimal("60.50"));
        dto.setCapacity(3);

        // Persisted result
        Room persisted = new Room(UUID.fromString(roomId), dto.getNumber(), dto.getPricePerNight(), dto.getCapacity(), RoomStatusEnum.AVAILABLE);
        updateRoomOutputPort.toReturn = persisted;

        Room result = useCase.handle(hotelId.toString(), roomId, dto);

        assertEquals(hotelId, findHotelByIdOutputPort.lastCalledId);
        assertEquals(hotelId.toString(), findRoomByHotelAndIdOutputPort.lastHotelId);
        assertEquals(roomId, findRoomByHotelAndIdOutputPort.lastRoomId);
        assertEquals("102", updateRoomOutputPort.lastArg.getNumber());
        assertEquals(new BigDecimal("60.50"), updateRoomOutputPort.lastArg.getPricePerNight());
        assertEquals(3, updateRoomOutputPort.lastArg.getCapacity());
        assertEquals(persisted, result);
    }

    @Test
    void handle_throwsNotFound_whenHotelMissing() {
        UUID hotelId = UUID.randomUUID();
        String roomId = UUID.randomUUID().toString();

        // No hotel
        findHotelByIdOutputPort.expectedId = hotelId;
        findHotelByIdOutputPort.toReturn = Optional.empty();

        UpdateRoomDTO dto = new UpdateRoomDTO();
        dto.setNumber("102");
        dto.setPricePerNight(new BigDecimal("60.50"));
        dto.setCapacity(3);

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId.toString(), roomId, dto));
    }

    @Test
    void handle_throwsNotFound_whenRoomMissing() {
        UUID hotelId = UUID.randomUUID();
        String roomId = UUID.randomUUID().toString();

        // Hotel exists
        findHotelByIdOutputPort.expectedId = hotelId;
        findHotelByIdOutputPort.toReturn = Optional.of(Hotel.create("H", "A", new BigDecimal("10.00")));

        // Room missing
        findRoomByHotelAndIdOutputPort.expectedHotelId = hotelId.toString();
        findRoomByHotelAndIdOutputPort.expectedRoomId = roomId;
        findRoomByHotelAndIdOutputPort.toReturn = Optional.empty();

        UpdateRoomDTO dto = new UpdateRoomDTO();
        dto.setNumber("102");
        dto.setPricePerNight(new BigDecimal("60.50"));
        dto.setCapacity(3);

        assertThrows(NotFoundException.class, () -> useCase.handle(hotelId.toString(), roomId, dto));
    }

    @Test
    void handle_throwsDuplicated_whenNumberExistsInHotel() throws Exception {
        UUID hotelId = UUID.randomUUID();
        String roomId = UUID.randomUUID().toString();

        // Hotel exists with rooms list
        Hotel hotel = Hotel.create("H", "A", new BigDecimal("10.00"));
        // Existing room in hotel (the one being updated)
        Room existing = new Room(UUID.fromString(roomId), "101", new BigDecimal("50.00"), 2, RoomStatusEnum.AVAILABLE);
        // Another room in same hotel with the conflicting number
        Room other = new Room(UUID.randomUUID(), "102", new BigDecimal("45.00"), 2, RoomStatusEnum.AVAILABLE);
        hotel.setRooms(java.util.Arrays.asList(existing, other));

        findHotelByIdOutputPort.expectedId = hotelId;
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);

        // Existing room is found by id
        findRoomByHotelAndIdOutputPort.expectedHotelId = hotelId.toString();
        findRoomByHotelAndIdOutputPort.expectedRoomId = roomId;
        findRoomByHotelAndIdOutputPort.toReturn = Optional.of(existing);

        // Update payload attempts to use an already existing number in the hotel
        UpdateRoomDTO dto = new UpdateRoomDTO();
        dto.setNumber("102"); // conflicts with `other`
        dto.setPricePerNight(new BigDecimal("60.50"));
        dto.setCapacity(3);

        assertThrows(DuplicatedEntryException.class, () -> useCase.handle(hotelId.toString(), roomId, dto));
    }
}
