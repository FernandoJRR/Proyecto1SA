package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.dtos.UpdateRestaurantDTO;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.UpdateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class UpdateRestaurantUseCaseTest {

    private static class FakeFindRestaurantByIdOutputPort implements FindRestaurantByIdOutputPort {
        String expectedId;
        String lastCalledId;
        Optional<Restaurant> toReturn = Optional.empty();

        @Override
        public Optional<Restaurant> findById(String id) {
            lastCalledId = id;
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeUpdateRestaurantOutputPort implements UpdateRestaurantOutputPort {
        Restaurant lastArg;
        Restaurant toReturn;

        @Override
        public Restaurant updateRestaurant(Restaurant restaurant) {
            lastArg = restaurant;
            return toReturn != null ? toReturn : restaurant;
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeUpdateRestaurantOutputPort updateRestaurantOutputPort;
    private UpdateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        updateRestaurantOutputPort = new FakeUpdateRestaurantOutputPort();
        useCase = new UpdateRestaurantUseCase(findRestaurantByIdOutputPort, updateRestaurantOutputPort);
    }

    @Test
    void handle_updatesFields_andDelegatesToOutputPort() throws Exception {
        String id = UUID.randomUUID().toString();
        Restaurant existing = Restaurant.create("Old R", "Old A", null);
        // ensure IDs match: Restaurant.create randomizes id; we need any value; use port behavior
        findRestaurantByIdOutputPort.expectedId = id;
        findRestaurantByIdOutputPort.toReturn = Optional.of(existing);

        UpdateRestaurantDTO dto = new UpdateRestaurantDTO();
        dto.setName("New R");
        dto.setAddress("New A");

        Restaurant returned = Restaurant.create(dto.getName(), dto.getAddress(), null);
        updateRestaurantOutputPort.toReturn = returned;

        Restaurant result = useCase.handle(id, dto);

        assertEquals(id, findRestaurantByIdOutputPort.lastCalledId);
        assertEquals("New R", updateRestaurantOutputPort.lastArg.getName());
        assertEquals("New A", updateRestaurantOutputPort.lastArg.getAddress());
        assertEquals(returned, result);
    }

    @Test
    void handle_throwsNotFound_whenRestaurantDoesNotExist() {
        String id = UUID.randomUUID().toString();
        findRestaurantByIdOutputPort.expectedId = id;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        UpdateRestaurantDTO dto = new UpdateRestaurantDTO();
        dto.setName("N");
        dto.setAddress("A");

        assertThrows(NotFoundException.class, () -> useCase.handle(id, dto));
    }
}

