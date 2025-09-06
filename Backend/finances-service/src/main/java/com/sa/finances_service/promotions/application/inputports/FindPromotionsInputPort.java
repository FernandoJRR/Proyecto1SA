package com.sa.finances_service.promotions.application.inputports;

import java.util.List;

import com.sa.finances_service.promotions.domain.Promotion;

public interface FindPromotionsInputPort {
    public List<Promotion> handle();
}
