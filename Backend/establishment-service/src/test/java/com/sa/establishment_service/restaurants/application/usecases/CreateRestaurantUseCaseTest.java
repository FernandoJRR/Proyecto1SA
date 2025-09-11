package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.application.outputports.CreateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class CreateRestaurantUseCaseTest {

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

    private static class FakeCreateRestaurantOutputPort implements CreateRestaurantOutputPort {
        Restaurant lastArg;
        Restaurant toReturn;
        @Override
        public Restaurant createRestaurant(Restaurant restaurant) {
            lastArg = restaurant;
            return toReturn != null ? toReturn : restaurant;
        }
    }

    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FakeCreateRestaurantOutputPort createRestaurantOutputPort;
    private CreateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        createRestaurantOutputPort = new FakeCreateRestaurantOutputPort();
        useCase = new CreateRestaurantUseCase(findHotelByIdOutputPort, createRestaurantOutputPort);
    }

    @Test
    void handle_withHotelId_createsAndDelegates() throws Exception {
        UUID hotelId = UUID.randomUUID();
        Hotel hotel = Hotel.create("Hotel Central", "Main St", new BigDecimal("100"));
        findHotelByIdOutputPort.expectedId = hotelId;
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);

        CreateRestaurantDTO dto = new CreateRestaurantDTO();
        dto.setName("R1");
        dto.setAddress("Addr");
        dto.setHotelId(hotelId.toString());

        Restaurant persisted = Restaurant.create("R1", "Addr", hotel);
        createRestaurantOutputPort.toReturn = persisted;

        Restaurant result = useCase.handle(dto);

        assertEquals(persisted, result);
        assertEquals("R1", createRestaurantOutputPort.lastArg.getName());
        assertEquals("Addr", createRestaurantOutputPort.lastArg.getAddress());
        assertEquals(hotel, createRestaurantOutputPort.lastArg.getHotel());
        assertEquals(hotelId, findHotelByIdOutputPort.lastCalledId);
    }

    @Test
    void handle_withoutHotelId_createsWithNullHotel() throws Exception {
        CreateRestaurantDTO dto = new CreateRestaurantDTO();
        dto.setName("R2");
        dto.setAddress("Addr2");
        dto.setHotelId(null);

        Restaurant result = useCase.handle(dto);

        assertEquals("R2", createRestaurantOutputPort.lastArg.getName());
        assertEquals("Addr2", createRestaurantOutputPort.lastArg.getAddress());
        // should not attempt to find hotel when hotelId is null
        assertEquals(null, findHotelByIdOutputPort.lastCalledId);
        // returns the same instance when output port returns null (defaults to arg)
        assertEquals(createRestaurantOutputPort.lastArg, result);
    }

    @Test
    void handle_throwsNotFound_whenHotelMissing() {
        UUID hotelId = UUID.randomUUID();
        findHotelByIdOutputPort.expectedId = hotelId;
        findHotelByIdOutputPort.toReturn = Optional.empty();

        CreateRestaurantDTO dto = new CreateRestaurantDTO();
        dto.setName("R3");
        dto.setAddress("Addr3");
        dto.setHotelId(hotelId.toString());

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }
}

