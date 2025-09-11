package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;

class FindPromotionByIdAdapterTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionRepositoryMapper promotionRepositoryMapper;
    private FindPromotionByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPromotionByIdAdapter(promotionRepository, promotionRepositoryMapper);
    }

    @Test
    void findById_mapsWhenPresent() {
        String id = UUID.randomUUID().toString();
        PromotionEntity entity = new PromotionEntity();
        Promotion domain = new Promotion();
        when(promotionRepository.findById(id)).thenReturn(Optional.of(entity));
        when(promotionRepositoryMapper.toDomain(entity)).thenReturn(domain);

        var result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findById_emptyWhenMissing() {
        String id = UUID.randomUUID().toString();
        when(promotionRepository.findById(id)).thenReturn(Optional.empty());
        var result = adapter.findById(id);
        assertTrue(result.isEmpty());
    }
}

