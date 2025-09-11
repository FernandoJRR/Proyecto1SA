package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.application.dtos.FindOrderEligibilityDTO;
import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndRestaurantOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostFrequentClientsOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostPopularDishesOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.NotFoundException;

class FindEligiblePromotionOrderUseCaseTest {

    private static class FakeFindPromotionsByDateAndRestaurantOutputPort implements FindPromotionsByDateAndRestaurantOutputPort {
        List<Promotion> toReturn = List.of();
        @Override
        public List<Promotion> findByStartDateAndRestaurant(LocalDate startDate, String restaurantId) {
            return toReturn;
        }
    }

    private static class FakeMostFrequentClientsOutputPort implements MostFrequentClientsOutputPort {
        List<UUID> toReturn = List.of();
        @Override
        public List<UUID> findMostFrequent(Integer limit) { return toReturn; }
    }

    private static class FakeMostPopularDishesOutputPort implements MostPopularDishesOutputPort {
        List<UUID> toReturn = List.of();
        @Override
        public List<UUID> findMostPopular(String restaurantId, Integer limit) { return toReturn; }
    }

    private FakeFindPromotionsByDateAndRestaurantOutputPort findPromotionsByDateAndRestaurantOutputPort;
    private FakeMostFrequentClientsOutputPort mostFrequentClientsOutputPort;
    private FakeMostPopularDishesOutputPort mostPopularDishesOutputPort;
    private FindEligiblePromotionOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        findPromotionsByDateAndRestaurantOutputPort = new FakeFindPromotionsByDateAndRestaurantOutputPort();
        mostFrequentClientsOutputPort = new FakeMostFrequentClientsOutputPort();
        mostPopularDishesOutputPort = new FakeMostPopularDishesOutputPort();
        useCase = new FindEligiblePromotionOrderUseCase(
            findPromotionsByDateAndRestaurantOutputPort,
            mostFrequentClientsOutputPort,
            mostPopularDishesOutputPort
        );
    }

    @Test
    void handle_throwsNotFound_whenNoPromotionsFound() {
        findPromotionsByDateAndRestaurantOutputPort.toReturn = List.of();
        FindOrderEligibilityDTO dto = new FindOrderEligibilityDTO("c1", "r1", List.of("d1"), LocalDate.now());
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_selectsHighestPercentageAmongEligible_frequentClient() throws Exception {
        UUID clientId = UUID.randomUUID();
        Promotion p1 = Promotion.create(new BigDecimal("5"), LocalDate.now(), LocalDate.now().plusDays(1), "P1", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 3, PromotionType.CLIENT_MOST_FREQUENT);
        Promotion p2 = Promotion.create(new BigDecimal("10"), LocalDate.now(), LocalDate.now().plusDays(1), "P2", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 3, PromotionType.CLIENT_MOST_FREQUENT);
        findPromotionsByDateAndRestaurantOutputPort.toReturn = List.of(p1, p2);

        mostFrequentClientsOutputPort.toReturn = List.of(clientId);

        FindOrderEligibilityDTO dto = new FindOrderEligibilityDTO(clientId.toString(), "r1", List.of("d1"), LocalDate.now());
        Promotion result = useCase.handle(dto);

        assertEquals(p2, result);
    }

    @Test
    void handle_dishMostPopularEligible_whenOrderContainsPopularDish() throws Exception {
        UUID dishId = UUID.randomUUID();
        Promotion pDish = Promotion.create(new BigDecimal("7"), LocalDate.now(), LocalDate.now().plusDays(1), "PD", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 5, PromotionType.DISH_MOST_POPULAR);
        findPromotionsByDateAndRestaurantOutputPort.toReturn = List.of(pDish);
        mostPopularDishesOutputPort.toReturn = List.of(dishId);

        FindOrderEligibilityDTO dto = new FindOrderEligibilityDTO(UUID.randomUUID().toString(), "r1", List.of(dishId.toString(), UUID.randomUUID().toString()), LocalDate.now());
        Promotion result = useCase.handle(dto);

        assertEquals(pDish, result);
    }

    @Test
    void handle_throwsNotFound_whenNoneEligible() {
        Promotion pDish = Promotion.create(new BigDecimal("7"), LocalDate.now(), LocalDate.now().plusDays(1), "PD", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 5, PromotionType.DISH_MOST_POPULAR);
        findPromotionsByDateAndRestaurantOutputPort.toReturn = List.of(pDish);
        // mostPopularDishes does not contain order dishes
        mostPopularDishesOutputPort.toReturn = List.of(UUID.randomUUID());

        FindOrderEligibilityDTO dto = new FindOrderEligibilityDTO(UUID.randomUUID().toString(), "r1", List.of(UUID.randomUUID().toString()), LocalDate.now());
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }
}

