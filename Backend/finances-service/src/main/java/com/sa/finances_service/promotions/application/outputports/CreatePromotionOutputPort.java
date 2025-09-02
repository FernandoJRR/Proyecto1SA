package com.sa.finances_service.promotions.application.outputports;

import com.sa.finances_service.promotions.domain.Promotion;

public interface CreatePromotionOutputPort {
    public Promotion createPromotion(Promotion promotion);
}
