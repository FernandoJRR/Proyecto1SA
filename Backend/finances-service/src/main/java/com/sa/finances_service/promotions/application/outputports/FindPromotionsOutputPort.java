package com.sa.finances_service.promotions.application.outputports;

import java.util.List;

import com.sa.finances_service.promotions.domain.Promotion;

public interface FindPromotionsOutputPort {
    public List<Promotion> findAll();
}
