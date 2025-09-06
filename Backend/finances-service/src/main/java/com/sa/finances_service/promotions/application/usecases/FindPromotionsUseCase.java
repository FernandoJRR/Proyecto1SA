package com.sa.finances_service.promotions.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.inputports.FindPromotionsInputPort;
import com.sa.finances_service.promotions.application.outputports.FindPromotionsOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindPromotionsUseCase implements FindPromotionsInputPort {

    private final FindPromotionsOutputPort findPromotionsOutputPort;

    @Override
    public List<Promotion> handle() {
        return findPromotionsOutputPort.findAll();
    }

}
