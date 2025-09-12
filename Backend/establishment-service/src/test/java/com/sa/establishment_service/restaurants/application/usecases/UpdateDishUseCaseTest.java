package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.dtos.UpdateDishDTO;
import com.sa.establishment_service.restaurants.application.outputports.FindDishByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.UpdateDishOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class UpdateDishUseCaseTest {

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

    private static class FakeFindDishByIdOutputPort implements FindDishByIdOutputPort {
        String expectedId;
        String lastCalledId;
        Optional<Dish> toReturn = Optional.empty();
        @Override
        public Optional<Dish> findById(String id) {
            lastCalledId = id;
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeUpdateDishOutputPort implements UpdateDishOutputPort {
        Dish lastArg;
        Dish toReturn;
        @Override
        public Dish updateDish(Dish dish) {
            lastArg = dish;
            return toReturn != null ? toReturn : dish;
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeFindDishByIdOutputPort findDishByIdOutputPort;
    private FakeUpdateDishOutputPort updateDishOutputPort;
    private UpdateDishUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        findDishByIdOutputPort = new FakeFindDishByIdOutputPort();
        updateDishOutputPort = new FakeUpdateDishOutputPort();
        useCase = new UpdateDishUseCase(findRestaurantByIdOutputPort, findDishByIdOutputPort, updateDishOutputPort);
    }

    @Test
    void handle_updatesFields_andDelegatesToOutputPort() throws Exception {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();

        // Restaurant exists
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));

        // Existing dish
        Dish existing = new Dish(UUID.fromString(dishId), "Old", new BigDecimal("5.00"));
        findDishByIdOutputPort.expectedId = dishId;
        findDishByIdOutputPort.toReturn = Optional.of(existing);

        // Update payload
        UpdateDishDTO dto = new UpdateDishDTO();
        dto.setName("New");
        dto.setPrice(new BigDecimal("6.50"));

        // Persisted result
        Dish persisted = new Dish(UUID.fromString(dishId), dto.getName(), dto.getPrice());
        updateDishOutputPort.toReturn = persisted;

        Dish result = useCase.handle(restaurantId, dishId, dto);

        assertEquals(restaurantId, findRestaurantByIdOutputPort.lastCalledId);
        assertEquals(dishId, findDishByIdOutputPort.lastCalledId);
        assertEquals("New", updateDishOutputPort.lastArg.getName());
        assertEquals(new BigDecimal("6.50"), updateDishOutputPort.lastArg.getPrice());
        assertEquals(persisted, result);
    }

    @Test
    void handle_throwsNotFound_whenRestaurantMissing() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();

        // Restaurant missing
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        UpdateDishDTO dto = new UpdateDishDTO();
        dto.setName("New");
        dto.setPrice(new BigDecimal("6.50"));

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId, dishId, dto));
    }

    @Test
    void handle_throwsNotFound_whenDishMissing() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();

        // Restaurant exists
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));

        // Dish missing
        findDishByIdOutputPort.expectedId = dishId;
        findDishByIdOutputPort.toReturn = Optional.empty();

        UpdateDishDTO dto = new UpdateDishDTO();
        dto.setName("New");
        dto.setPrice(new BigDecimal("6.50"));

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId, dishId, dto));
    }
}

