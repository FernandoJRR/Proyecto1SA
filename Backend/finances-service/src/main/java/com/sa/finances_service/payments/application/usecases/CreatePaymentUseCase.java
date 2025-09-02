package com.sa.finances_service.payments.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finances_service.payments.application.outputports.CreatePaymentOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.promotions.application.outputports.FindPromotionByIdOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreatePaymentUseCase implements CreatePaymentInputPort {

    private final CreatePaymentOutputPort createPaymentOutputPort;
    private final FindPromotionByIdOutputPort findPromotionByIdOutputPort;

    @Override
    public Payment handle(CreatePaymentDTO createPaymentDTO) throws NotFoundException {
        System.out.println("CREATIIING"+ createPaymentDTO);
        Payment createdPayment = Payment.create(createPaymentDTO.getEstablishmentId(),
                createPaymentDTO.getClientId(), SourceTypeEnum.valueOf(createPaymentDTO.getSourceType()),
                createPaymentDTO.getSourceId(),
                createPaymentDTO.getSubtotal(), PaymentMethodEnum.valueOf(createPaymentDTO.getMethod()),
                createPaymentDTO.getCardNumber());

        if (createPaymentDTO.getPromotionId() != null) {
            Promotion promotion = findPromotionByIdOutputPort
                    .findById(createPaymentDTO.getPromotionId().toString())
                    .orElseThrow(() -> new NotFoundException("La promocion ingresada no existe."));

            createdPayment.applyPromotion(promotion.getId().toString(), promotion.getPromotionType().getName(),
                    promotion.getPercentage());
        }

        return createPaymentOutputPort.createPayment(createdPayment);
    }

}
