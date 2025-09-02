package com.sa.client_service.shared.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.client_service.shared.domain.PromotionApplied;
import com.sa.client_service.shared.infrastructure.repositoryadapter.models.PromotionAppliedEntity;

@Mapper(componentModel = "spring")
public interface PromotionRepositoryMapper {
    public PromotionApplied toDomain(PromotionAppliedEntity promotionAppliedEntity);
    public PromotionAppliedEntity toEntity(PromotionApplied promotionApplied);
}
