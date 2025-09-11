package com.sa.finances_service.promotions.application.inputports;

import com.sa.finances_service.promotions.application.dtos.FindOrderEligibilityDTO;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface FindEligiblePromotionOrderInputPort {
    public Promotion handle(@Valid FindOrderEligibilityDTO dto) throws NotFoundException;
}
