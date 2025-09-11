package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.domain.PromotionType;

class FindAllPromotionTypesUseCaseTest {

    @Test
    void handle_returnsAllPromotionTypes() {
        FindAllPromotionTypesUseCase useCase = new FindAllPromotionTypesUseCase();
        assertEquals(PromotionType.getAll().size(), useCase.handle().size());
    }
}

