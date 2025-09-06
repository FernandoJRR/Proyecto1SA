package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import java.util.List;

import com.sa.finances_service.promotions.application.outputports.FindPromotionsOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPromotionsAdapter implements FindPromotionsOutputPort {

    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryMapper promotionRepositoryMapper;

    @Override
    public List<Promotion> findAll() {
        List<PromotionEntity> promotionEntities = promotionRepository.findAll();
        return promotionRepositoryMapper.toDomain(promotionEntities);
    }

}
