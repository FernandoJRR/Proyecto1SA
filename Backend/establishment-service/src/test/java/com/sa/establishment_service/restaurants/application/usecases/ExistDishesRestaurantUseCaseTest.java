package com.sa.establishment_service.restaurants.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.restaurants.application.dtos.ExistDishesDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesResultDTO;
import com.sa.establishment_service.restaurants.application.outputports.ExistDishesRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;

class ExistDishesRestaurantUseCaseTest {

    private static class FakeExistDishesRestaurantOutputPort implements ExistDishesRestaurantOutputPort {
        String lastRestaurantId;
        List<String> lastIds;
        List<Dish> toReturn = List.of();
        @Override
        public List<Dish> findDishesByRestaurantAndIds(String restaurantId, List<String> presentIds) {
            lastRestaurantId = restaurantId;
            lastIds = presentIds;
            return toReturn;
        }
    }

    private FakeExistDishesRestaurantOutputPort existDishesRestaurantOutputPort;
    private ExistDishesRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        existDishesRestaurantOutputPort = new FakeExistDishesRestaurantOutputPort();
        useCase = new ExistDishesRestaurantUseCase(existDishesRestaurantOutputPort);
    }

    @Test
    void handle_allPresent_returnsAllPresentTrue() {
        UUID restaurantId = UUID.randomUUID();
        UUID d1 = UUID.randomUUID();
        UUID d2 = UUID.randomUUID();
        List<UUID> ids = Arrays.asList(d1, d2);

        Dish dish1 = new Dish(d1, "D1", new BigDecimal("5.00"));
        Dish dish2 = new Dish(d2, "D2", new BigDecimal("6.00"));
        existDishesRestaurantOutputPort.toReturn = List.of(dish1, dish2);

        ExistDishesDTO dto = new ExistDishesDTO();
        dto.setRestaurantId(restaurantId);
        dto.setDishIds(ids);

        ExistDishesResultDTO result = useCase.handle(dto);

        assertTrue(result.isAllPresent());
        assertTrue(result.getMissingIds().isEmpty());
        assertEquals(2, result.getPresentDishes().size());
        assertEquals(restaurantId.toString(), existDishesRestaurantOutputPort.lastRestaurantId);
        assertEquals(List.of(d1.toString(), d2.toString()), existDishesRestaurantOutputPort.lastIds);
    }

    @Test
    void handle_someMissing_listsMissingIds() {
        UUID restaurantId = UUID.randomUUID();
        UUID d1 = UUID.randomUUID();
        UUID d2 = UUID.randomUUID();
        UUID d3 = UUID.randomUUID();
        List<UUID> ids = Arrays.asList(d1, d2, d3);

        Dish dish1 = new Dish(d1, "D1", new BigDecimal("5.00"));
        Dish dish3 = new Dish(d3, "D3", new BigDecimal("7.00"));
        existDishesRestaurantOutputPort.toReturn = List.of(dish1, dish3);

        ExistDishesDTO dto = new ExistDishesDTO();
        dto.setRestaurantId(restaurantId);
        dto.setDishIds(ids);

        ExistDishesResultDTO result = useCase.handle(dto);

        assertEquals(false, result.isAllPresent());
        assertEquals(List.of(d2), result.getMissingIds());
        assertEquals(2, result.getPresentDishes().size());
    }
}

