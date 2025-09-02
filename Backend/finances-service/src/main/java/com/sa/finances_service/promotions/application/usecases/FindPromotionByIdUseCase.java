package com.sa.finances_service.promotions.application.usecases;

import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.inputports.FindPromotionByIdInputPort;
import com.sa.finances_service.promotions.application.outputports.FindPromotionByIdOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindPromotionByIdUseCase implements FindPromotionByIdInputPort {

    private final FindPromotionByIdOutputPort findPromotionByIdOutputPort;

    @Override
    public Promotion handle(UUID id) throws NotFoundException {
        return findPromotionByIdOutputPort.findById(id.toString())
            .orElseThrow(() -> new NotFoundException("La promocion buscada no existe."));
    }

}
