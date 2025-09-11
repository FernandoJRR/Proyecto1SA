package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByHotelOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class FindRestaurantsByHotelUseCaseTest {

    private static class FakeExistsHotelByIdOutputPort implements ExistsHotelByIdOutputPort {
        boolean exists;
        @Override
        public boolean existsById(String id) { return exists; }
    }

    private static class FakeFindRestaurantByHotelOutputPort implements FindRestaurantByHotelOutputPort {
        String lastHotelId;
        List<Restaurant> toReturn = List.of();
        @Override
        public List<Restaurant> findByHotel(String hotelId) {
            lastHotelId = hotelId;
            return toReturn;
        }
    }

    private FakeExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private FakeFindRestaurantByHotelOutputPort findRestaurantByHotelOutputPort;
    private FindRestaurantsByHotelUseCase useCase;

    @BeforeEach
    void setUp() {
        existsHotelByIdOutputPort = new FakeExistsHotelByIdOutputPort();
        findRestaurantByHotelOutputPort = new FakeFindRestaurantByHotelOutputPort();
        useCase = new FindRestaurantsByHotelUseCase(existsHotelByIdOutputPort, findRestaurantByHotelOutputPort);
    }

    @Test
    void handle_returnsList_whenHotelExists() throws Exception {
        String hotelId = "h1";
        existsHotelByIdOutputPort.exists = true;
        List<Restaurant> restaurants = List.of(new Restaurant(null, null, null, null));
        findRestaurantByHotelOutputPort.toReturn = restaurants;

        List<Restaurant> result = useCase.handle(hotelId);

        assertEquals(restaurants, result);
        assertEquals(hotelId, findRestaurantByHotelOutputPort.lastHotelId);
    }

    @Test
    void handle_throwsNotFound_whenHotelMissing() {
        existsHotelByIdOutputPort.exists = false;
        assertThrows(NotFoundException.class, () -> useCase.handle("h2"));
    }
}

