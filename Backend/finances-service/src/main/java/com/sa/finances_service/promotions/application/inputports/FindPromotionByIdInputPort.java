package com.sa.finances_service.promotions.application.inputports;

import java.util.UUID;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.NotFoundException;

public interface FindPromotionByIdInputPort {
    public Promotion handle(UUID id) throws NotFoundException;
}
