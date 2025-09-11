package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;

class CreatePromotionAdapterTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionRepositoryMapper promotionRepositoryMapper;

    private CreatePromotionAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreatePromotionAdapter(promotionRepository, promotionRepositoryMapper);
    }

    @Test
    void createPromotion_mapsAndPersists_thenMapsBack() {
        Promotion domain = Promotion.create(new BigDecimal("10.00"), LocalDate.now(), LocalDate.now().plusDays(1),
            "Promo", UUID.randomUUID(), EstablishmentTypeEnum.RESTAURANT, 3, PromotionType.DISH_MOST_POPULAR);
        PromotionEntity entity = new PromotionEntity();
        PromotionEntity saved = new PromotionEntity();
        Promotion mappedBack = domain;

        when(promotionRepositoryMapper.toEntity(domain)).thenReturn(entity);
        when(promotionRepository.save(entity)).thenReturn(saved);
        when(promotionRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Promotion result = adapter.createPromotion(domain);

        assertEquals(mappedBack, result);
        verify(promotionRepositoryMapper).toEntity(domain);
        verify(promotionRepository).save(entity);
        verify(promotionRepositoryMapper).toDomain(saved);
    }
}

