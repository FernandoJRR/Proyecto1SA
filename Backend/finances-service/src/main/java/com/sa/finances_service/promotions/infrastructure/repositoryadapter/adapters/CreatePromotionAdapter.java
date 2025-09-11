package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.finances_service.promotions.application.outputports.CreatePromotionOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreatePromotionAdapter implements CreatePromotionOutputPort {

    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryMapper promotionRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Promotion createPromotion(Promotion promotion) {
        PromotionEntity promotionEntity = promotionRepositoryMapper.toEntity(promotion);
        PromotionEntity createdPromotion = promotionRepository.save(promotionEntity);
        return promotionRepositoryMapper.toDomain(createdPromotion);
    }

}
