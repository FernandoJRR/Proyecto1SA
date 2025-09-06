package com.sa.finances_service.promotions.application.usecases;

import java.time.LocalDate;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.application.inputports.CreatePromotionInputPort;
import com.sa.finances_service.promotions.application.outputports.CreatePromotionOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
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

        System.out.println("PASAAAA");
        EstablishmentTypeEnum establishmentType;
        try {
            establishmentType = EstablishmentTypeEnum.valueOf(createPromotionDTO.getEstablishmentType());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("El tipo de establecimiento ingresado no existe");
        }

        if (establishmentType.equals(EstablishmentTypeEnum.HOTEL) && promotionType.equals(PromotionType.DISH_MOST_POPULAR)) {
            throw new InvalidParameterException("El tipo de promocion no es aplicable a un Hotel");
        } else if (establishmentType.equals(EstablishmentTypeEnum.RESTAURANT) && promotionType.equals(PromotionType.ROOM_MOST_POPULAR)) {
            throw new InvalidParameterException("El tipo de promocion no es aplicable a un Restaurante");
        }

        System.out.println("AQUIIII");
        Promotion createdPromotion = Promotion.create(
            createPromotionDTO.getPercentage(),
            createPromotionDTO.getStartDate(),
            createPromotionDTO.getEndDate(),
            createPromotionDTO.getName(),
            UUID.fromString(createPromotionDTO.getEstablishmentId()),
            establishmentType,
            createPromotionDTO.getTopCount(),
            promotionType);
        System.out.println("PAASAAAA");

        return createPromotionOutputPort.createPromotion(createdPromotion);
    }

}
