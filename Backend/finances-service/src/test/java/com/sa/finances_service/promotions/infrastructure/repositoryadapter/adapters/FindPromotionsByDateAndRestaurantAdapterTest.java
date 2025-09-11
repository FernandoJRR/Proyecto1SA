package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;

class FindPromotionsByDateAndRestaurantAdapterTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionRepositoryMapper promotionRepositoryMapper;
    private FindPromotionsByDateAndRestaurantAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPromotionsByDateAndRestaurantAdapter(promotionRepository, promotionRepositoryMapper);
    }

    @Test
    void findByStartDateAndRestaurant_delegatesToRepoAndMaps() {
        String restaurantId = "r1";
        LocalDate at = LocalDate.now();
        List<PromotionEntity> entities = List.of(new PromotionEntity());
        List<Promotion> domain = List.of(new Promotion());
        when(promotionRepository.findActiveForRestaurant(restaurantId, at)).thenReturn(entities);
        when(promotionRepositoryMapper.toDomain(entities)).thenReturn(domain);

        List<Promotion> result = adapter.findByStartDateAndRestaurant(at, restaurantId);
        assertEquals(domain, result);
    }
}

