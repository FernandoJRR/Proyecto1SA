package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class FindRestaurantByIdUseCaseTest {

    private static class FakeFindRestaurantByIdOutputPort implements FindRestaurantByIdOutputPort {
        String expectedId;
        Optional<Restaurant> toReturn = Optional.empty();
        @Override
        public Optional<Restaurant> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FindRestaurantByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        useCase = new FindRestaurantByIdUseCase(findRestaurantByIdOutputPort);
    }

    @Test
    void handle_returnsRestaurant_whenFound() throws Exception {
        String id = "r1";
        Restaurant r = new Restaurant(null, null, null, null);
        findRestaurantByIdOutputPort.expectedId = id;
        findRestaurantByIdOutputPort.toReturn = Optional.of(r);

        Restaurant result = useCase.handle(id);

        assertEquals(r, result);
    }

    @Test
    void handle_throwsNotFound_whenMissing() {
        String id = "r-x";
        findRestaurantByIdOutputPort.expectedId = id;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(id));
    }
}

