package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.application.outputports.FindPromotionsOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;

class FindPromotionsUseCaseTest {

    private static class FakeFindPromotionsOutputPort implements FindPromotionsOutputPort {
        List<Promotion> toReturn = List.of();
        @Override
        public List<Promotion> findAll() { return toReturn; }
    }

    private FakeFindPromotionsOutputPort findPromotionsOutputPort;
    private FindPromotionsUseCase useCase;

    @BeforeEach
    void setUp() {
        findPromotionsOutputPort = new FakeFindPromotionsOutputPort();
        useCase = new FindPromotionsUseCase(findPromotionsOutputPort);
    }

    @Test
    void handle_returnsListFromOutputPort() {
        List<Promotion> promos = List.of(
            Promotion.create(new BigDecimal("5"), LocalDate.now(), LocalDate.now().plusDays(1), "P1", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 2, PromotionType.DISH_MOST_POPULAR),
            Promotion.create(new BigDecimal("12"), LocalDate.now(), LocalDate.now().plusDays(1), "P2", UUID.randomUUID(), EstablishmentTypeEnum.HOTEL, 3, PromotionType.ROOM_MOST_POPULAR)
        );
        findPromotionsOutputPort.toReturn = promos;

        List<Promotion> result = useCase.handle();
        assertEquals(promos, result);
        assertEquals(2, result.size());
    }
}

