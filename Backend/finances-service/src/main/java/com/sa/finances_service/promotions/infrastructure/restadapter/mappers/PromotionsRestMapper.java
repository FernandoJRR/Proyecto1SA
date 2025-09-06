package com.sa.finances_service.promotions.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.application.dtos.FindOrderEligibilityDTO;
import com.sa.finances_service.promotions.application.dtos.FindReservationEligibilityDTO;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.domain.PromotionType.PromotionTypeInfo;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.CreatePromotionRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.FindOrderEligibilityRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.FindReservationEligibilityRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.PromotionResponse;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.PromotionTypeResponse;

@Mapper(componentModel = "spring")
public interface PromotionsRestMapper {
    public CreatePromotionDTO toDTO(CreatePromotionRequest createPromotionRequest);
    public PromotionResponse toResponse(Promotion promotion);
    public List<PromotionResponse> toResponseList(List<Promotion> promotion);
    public PromotionTypeResponse toResponse(PromotionTypeInfo promotionType);
    public List<PromotionTypeResponse> toResponse(List<PromotionTypeInfo> promotionTypes);

    public FindReservationEligibilityDTO toDTO(FindReservationEligibilityRequest request);
    public FindOrderEligibilityDTO toDTO(FindOrderEligibilityRequest request);
}
