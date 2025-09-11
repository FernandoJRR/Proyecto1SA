package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.outputports.FindDishByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class FindDishByIdUseCaseTest {

    private static class FakeFindRestaurantByIdOutputPort implements FindRestaurantByIdOutputPort {
        String expectedId;
        Optional<Restaurant> toReturn = Optional.empty();
        @Override
        public Optional<Restaurant> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeFindDishByIdOutputPort implements FindDishByIdOutputPort {
        String expectedId;
        Optional<Dish> toReturn = Optional.empty();
        @Override
        public Optional<Dish> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeFindDishByIdOutputPort findDishByIdOutputPort;
    private FindDishByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        findDishByIdOutputPort = new FakeFindDishByIdOutputPort();
        useCase = new FindDishByIdUseCase(findDishByIdOutputPort, findRestaurantByIdOutputPort);
    }

    @Test
    void handle_returnsDish_whenFound() throws Exception {
        String restaurantId = "r1";
        String dishId = "d1";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));
        Dish dish = Dish.create("Soup", new BigDecimal("3.50"));
        findDishByIdOutputPort.expectedId = dishId;
        findDishByIdOutputPort.toReturn = Optional.of(dish);

        Dish result = useCase.handle(restaurantId, dishId);

        assertEquals(dish, result);
    }

    @Test
    void handle_throwsNotFound_whenRestaurantMissing() {
        String restaurantId = "r-notfound";
        String dishId = "dX";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId, dishId));
    }

    @Test
    void handle_throwsNotFound_whenDishMissing() {
        String restaurantId = "r1";
        String dishId = "d-missing";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));
        findDishByIdOutputPort.expectedId = dishId;
        findDishByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId, dishId));
    }
}

