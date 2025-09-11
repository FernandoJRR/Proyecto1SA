package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.dtos.CreateDishDTO;
import com.sa.establishment_service.restaurants.application.outputports.CreateDishOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

class CreateDishUseCaseTest {

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

    private static class FakeCreateDishOutputPort implements CreateDishOutputPort {
        String lastRestaurantId;
        Dish lastDish;
        Dish toReturn;
        @Override
        public Dish createDish(String restaurantId, Dish dish) throws NotFoundException {
            lastRestaurantId = restaurantId;
            lastDish = dish;
            return toReturn != null ? toReturn : dish;
        }
    }

    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeCreateDishOutputPort createDishOutputPort;
    private CreateDishUseCase useCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        createDishOutputPort = new FakeCreateDishOutputPort();
        useCase = new CreateDishUseCase(createDishOutputPort, findRestaurantByIdOutputPort);
    }

    @Test
    void handle_createsDishAndDelegates() throws Exception {
        String restaurantId = "rest-1";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(null, null, null, null));

        CreateDishDTO dto = new CreateDishDTO();
        dto.setName("Spaghetti");
        dto.setPrice(new BigDecimal("12.50"));

        Dish persisted = Dish.create(dto.getName(), dto.getPrice());
        createDishOutputPort.toReturn = persisted;

        Dish result = useCase.handle(restaurantId, dto);

        assertEquals(persisted, result);
        assertEquals(restaurantId, createDishOutputPort.lastRestaurantId);
        assertEquals("Spaghetti", createDishOutputPort.lastDish.getName());
        assertEquals(new BigDecimal("12.50"), createDishOutputPort.lastDish.getPrice());
        assertEquals(restaurantId, findRestaurantByIdOutputPort.lastCalledId);
    }

    @Test
    void handle_throwsNotFound_whenRestaurantMissing() {
        String restaurantId = "rest-2";
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        CreateDishDTO dto = new CreateDishDTO();
        dto.setName("Pizza");
        dto.setPrice(new BigDecimal("9.99"));

        assertThrows(NotFoundException.class, () -> useCase.handle(restaurantId, dto));
    }
}

