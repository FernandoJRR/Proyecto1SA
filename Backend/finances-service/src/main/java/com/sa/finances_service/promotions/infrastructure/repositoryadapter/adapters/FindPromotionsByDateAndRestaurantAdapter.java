package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import java.time.LocalDate;
import java.util.List;

import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndRestaurantOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPromotionsByDateAndRestaurantAdapter implements FindPromotionsByDateAndRestaurantOutputPort {

    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryMapper promotionRepositoryMapper;

    @Override
    public List<Promotion> findByStartDateAndRestaurant(LocalDate startDate, String restaurantId) {
        List<PromotionEntity> result = promotionRepository.findActiveForRestaurant(restaurantId, startDate);
        return promotionRepositoryMapper.toDomain(result);
    }

}
