package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndHotelOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPromotionsByDateAndHotelAdapter implements FindPromotionsByDateAndHotelOutputPort {

    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryMapper promotionRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> findByStartDateAndHotel(LocalDate startDate, String hotelId) {
        List<PromotionEntity> result = promotionRepository.findActiveForHotel(hotelId, startDate);
        return promotionRepositoryMapper.toDomain(result);
    }

}
