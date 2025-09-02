package com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;

@Mapper(componentModel = "spring")
public interface PromotionRepositoryMapper {
    public PromotionEntity toEntity(Promotion promotion);
    public Promotion toDomain(PromotionEntity promotionEntity);
}
