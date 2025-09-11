package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;

class FindPromotionsAdapterTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionRepositoryMapper promotionRepositoryMapper;
    private FindPromotionsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPromotionsAdapter(promotionRepository, promotionRepositoryMapper);
    }

    @Test
    void findAll_mapsList() {
        List<PromotionEntity> entities = List.of(new PromotionEntity(), new PromotionEntity());
        List<Promotion> domain = List.of(new Promotion(), new Promotion());
        when(promotionRepository.findAll()).thenReturn(entities);
        when(promotionRepositoryMapper.toDomain(entities)).thenReturn(domain);

        List<Promotion> result = adapter.findAll();
        assertEquals(domain, result);
        assertEquals(2, result.size());
    }
}

