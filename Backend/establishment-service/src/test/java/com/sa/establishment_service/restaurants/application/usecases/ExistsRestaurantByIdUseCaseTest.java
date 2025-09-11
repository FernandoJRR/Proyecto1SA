package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class ExistsRestaurantByIdUseCaseTest {

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
    private ExistsRestaurantByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        useCase = new ExistsRestaurantByIdUseCase(findRestaurantByIdOutputPort);
    }

    @Test
    void handle_doesNothing_whenRestaurantExists() {
        findRestaurantByIdOutputPort.expectedId = "r1";
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));
        assertDoesNotThrow(() -> useCase.handle("r1"));
    }

    @Test
    void handle_throwsNotFound_whenRestaurantMissing() {
        findRestaurantByIdOutputPort.expectedId = "r2";
        findRestaurantByIdOutputPort.toReturn = Optional.empty();
        assertThrows(NotFoundException.class, () -> useCase.handle("r2"));
    }
}

