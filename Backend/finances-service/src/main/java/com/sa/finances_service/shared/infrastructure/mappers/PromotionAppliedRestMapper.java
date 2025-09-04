package com.sa.finances_service.shared.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.sa.finances_service.shared.domain.PromotionApplied;
import com.sa.finances_service.shared.infrastructure.dtos.PromotionAppliedResponse;

@Mapper(componentModel = "spring")
public interface PromotionAppliedRestMapper {
    public PromotionAppliedResponse toResponse(PromotionApplied promotionApplied);
}
