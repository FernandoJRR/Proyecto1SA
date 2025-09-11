package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.application.outputports.FindPromotionByIdOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.NotFoundException;

class FindPromotionByIdUseCaseTest {

    private static class FakeFindPromotionByIdOutputPort implements FindPromotionByIdOutputPort {
        String expectedId;
        Optional<Promotion> toReturn = Optional.empty();
        @Override
        public Optional<Promotion> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeFindPromotionByIdOutputPort findPromotionByIdOutputPort;
    private FindPromotionByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findPromotionByIdOutputPort = new FakeFindPromotionByIdOutputPort();
        useCase = new FindPromotionByIdUseCase(findPromotionByIdOutputPort);
    }

    @Test
    void handle_returnsPromotion_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        Promotion promo = Promotion.create(new BigDecimal("10"), LocalDate.now(), LocalDate.now().plusDays(1), "P", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 2, PromotionType.DISH_MOST_POPULAR);
        findPromotionByIdOutputPort.expectedId = id.toString();
        findPromotionByIdOutputPort.toReturn = Optional.of(promo);

        Promotion result = useCase.handle(id);
        assertEquals(promo, result);
    }

    @Test
    void handle_throwsNotFound_whenMissing() {
        UUID id = UUID.randomUUID();
        findPromotionByIdOutputPort.expectedId = id.toString();
        findPromotionByIdOutputPort.toReturn = Optional.empty();
        assertThrows(NotFoundException.class, () -> useCase.handle(id));
    }
}

