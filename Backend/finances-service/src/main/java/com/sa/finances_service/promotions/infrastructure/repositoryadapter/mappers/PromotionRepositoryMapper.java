package com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;

@Mapper(componentModel = "spring")
public interface PromotionRepositoryMapper {
    public PromotionEntity toEntity(Promotion promotion);
    public Promotion toDomain(PromotionEntity promotionEntity);
    public List<Promotion> toDomain(List<PromotionEntity> promotionEntity);
}
