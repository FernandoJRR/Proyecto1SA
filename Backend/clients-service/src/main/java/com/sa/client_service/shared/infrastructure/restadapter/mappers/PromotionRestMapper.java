package com.sa.client_service.shared.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.client_service.shared.domain.PromotionApplied;
import com.sa.client_service.shared.infrastructure.restadapter.dtos.PromotionAppliedResponse;

@Mapper(componentModel = "spring")
public interface PromotionRestMapper {
    public PromotionAppliedResponse toResponse(PromotionApplied promotionApplied);
}
