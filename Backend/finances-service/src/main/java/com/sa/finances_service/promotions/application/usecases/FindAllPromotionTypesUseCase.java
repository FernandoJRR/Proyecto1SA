package com.sa.finances_service.promotions.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.inputports.FindAllPromotionTypesInputPort;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.domain.PromotionType.PromotionTypeInfo;

@UseCase
public class FindAllPromotionTypesUseCase implements FindAllPromotionTypesInputPort {

    @Override
    public List<PromotionTypeInfo> handle() {
        return PromotionType.getAll();
    }

}
