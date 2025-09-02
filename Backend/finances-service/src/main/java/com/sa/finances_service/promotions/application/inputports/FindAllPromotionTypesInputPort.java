package com.sa.finances_service.promotions.application.inputports;

import java.util.List;

import com.sa.finances_service.promotions.domain.PromotionType.PromotionTypeInfo;

public interface FindAllPromotionTypesInputPort {
    public List<PromotionTypeInfo> handle();
}
