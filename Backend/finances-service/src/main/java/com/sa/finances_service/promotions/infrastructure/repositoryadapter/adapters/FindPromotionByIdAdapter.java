package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.sa.finances_service.promotions.application.outputports.FindPromotionByIdOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPromotionByIdAdapter implements FindPromotionByIdOutputPort {

    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryMapper promotionRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Promotion> findById(String id) {
        return promotionRepository.findById(id)
            .map(promotionRepositoryMapper::toDomain);
    }

}
