package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.restaurants.application.outputports.FindAllRestaurantsOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;

class FindAllRestaurantsUseCaseTest {

    private static class FakeFindAllRestaurantsOutputPort implements FindAllRestaurantsOutputPort {
        List<Restaurant> toReturn = List.of();
        @Override
        public List<Restaurant> findAll() { return toReturn; }
    }

    private FakeFindAllRestaurantsOutputPort findAllRestaurantsOutputPort;
    private FindAllRestaurantsUseCase useCase;

    @BeforeEach
    void setUp() {
        findAllRestaurantsOutputPort = new FakeFindAllRestaurantsOutputPort();
        useCase = new FindAllRestaurantsUseCase(findAllRestaurantsOutputPort);
    }

    @Test
    void handle_returnsListFromOutputPort() {
        Hotel h = Hotel.create("H", "A", new BigDecimal("10"));
        List<Restaurant> restaurants = List.of(
            Restaurant.create("R1", "Addr1", h),
            Restaurant.create("R2", "Addr2", null)
        );
        findAllRestaurantsOutputPort.toReturn = restaurants;

        List<Restaurant> result = useCase.handle();

        assertEquals(restaurants, result);
        assertEquals(2, result.size());
    }
}

