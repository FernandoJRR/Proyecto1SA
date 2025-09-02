package com.sa.finances_service.promotions.application.usecases;

import java.time.LocalDate;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.application.inputports.CreatePromotionInputPort;
import com.sa.finances_service.promotions.application.outputports.CreatePromotionOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.InvalidParameterException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreatePromotionUseCase implements CreatePromotionInputPort {

    private final CreatePromotionOutputPort createPromotionOutputPort;

    @Override
    public Promotion handle(@Valid CreatePromotionDTO createPromotionDTO) throws InvalidParameterException {
        if (createPromotionDTO.getStartDate().isAfter(createPromotionDTO.getEndDate())) {
            throw new InvalidParameterException("Start date cannot be after end date.");
        }

        PromotionType promotionType;
        try {
            promotionType = PromotionType.valueOf(createPromotionDTO.getPromotionType());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("El tipo de promocion ingresada no existe");
        }

        Promotion createdPromotion = Promotion.create(createPromotionDTO.getPercentage(),
                createPromotionDTO.getStartDate(), createPromotionDTO.getEndDate(),
                promotionType);

        return createPromotionOutputPort.createPromotion(createdPromotion);
    }

}
