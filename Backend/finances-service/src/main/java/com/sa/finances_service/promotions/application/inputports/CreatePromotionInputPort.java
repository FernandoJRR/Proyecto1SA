package com.sa.finances_service.promotions.application.inputports;

import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.InvalidParameterException;

import jakarta.validation.Valid;

public interface CreatePromotionInputPort {
    public Promotion handle(@Valid CreatePromotionDTO createPromotionDTO) throws InvalidParameterException;
}
