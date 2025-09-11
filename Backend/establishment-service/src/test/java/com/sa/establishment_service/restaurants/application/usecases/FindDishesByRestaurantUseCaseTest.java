package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.outputports.FindDishesByRestaurantOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class FindDishesByRestaurantUseCaseTest {

    private static class FakeFindRestaurantByIdOutputPort implements FindRestaurantByIdOutputPort {
        String expectedId;
        Optional<Restaurant> toReturn = Optional.empty();
        @Override
        public Optional<Restaurant> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeFindDishesByRestaurantOutputPort implements FindDishesByRestaurantOutputPort {
        String lastRestaurantId;
        List<Dish> toReturn = List.of();
        @Override
        public List<Dish> findByRestaurant(String restaurantId) {
            lastRestaurantId = restaurantId;
            return toReturn;
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeFindDishesByRestaurantOutputPort findDishesByRestaurantOutputPort;
    private FindDishesByRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        findDishesByRestaurantOutputPort = new FakeFindDishesByRestaurantOutputPort();
        useCase = new FindDishesByRestaurantUseCase(findDishesByRestaurantOutputPort, findRestaurantByIdOutputPort);
    }

    @Test
    void handle_returnsList_whenRestaurantExists() throws Exception {
        String restaurantId = "r1";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));

        List<Dish> dishes = List.of(
            Dish.create("D1", new BigDecimal("1.00")),
            Dish.create("D2", new BigDecimal("2.00"))
        );
        findDishesByRestaurantOutputPort.toReturn = dishes;

        List<Dish> result = useCase.handle(restaurantId);

        assertEquals(dishes, result);
        assertEquals(restaurantId, findDishesByRestaurantOutputPort.lastRestaurantId);
    }

    @Test
    void handle_throwsNotFound_whenRestaurantMissing() {
        String restaurantId = "r2";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId));
    }
}

