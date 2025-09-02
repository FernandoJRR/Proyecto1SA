package com.sa.finances_service.promotions.application.outputports;

import java.util.Optional;

import com.sa.finances_service.promotions.domain.Promotion;

public interface FindPromotionByIdOutputPort {
    public Optional<Promotion> findById(String id);
}
