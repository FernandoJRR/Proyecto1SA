package com.sa.finances_service.payments.infrastructure.restadapter.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sa.finances_service.payments.application.dtos.HotelDTO;
import com.sa.finances_service.payments.application.dtos.RestaurantDTO;
import com.sa.finances_service.payments.application.outputports.PaymentHydrationOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentHydratedResponse;
import com.sa.finances_service.promotions.infrastructure.restadapter.mappers.PromotionsRestMapper;
import com.sa.finances_service.shared.infrastructure.mappers.PromotionAppliedRestMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentResponseAssembler {

    private final PaymentHydrationOutputPort paymentHydrationOutputPort;
    private final PromotionAppliedRestMapper promotionRestMapper;

    public PaymentHydratedResponse toResponse(Payment r) {
        if (r.getSourceType().equals(SourceTypeEnum.ORDER)) {
            RestaurantDTO restaurant = paymentHydrationOutputPort.getRestaurant(r.getEstablishmentId().toString());
            return new PaymentHydratedResponse(
                    r.getId().toString(),
                    r.getEstablishmentId(),
                    r.getClientId(),
                    r.getSourceType().toString(),
                    r.getSourceId(),
                    r.getSubtotal(),
                    r.getDiscount(),
                    r.getTotal(),
                    r.getMethod().toString(),
                    r.getStatus().toString(),
                    r.getCardNumber(),
                    promotionRestMapper.toResponse(r.getPromotionApplied()),
                    null,
                    restaurant
                    );
        } else {
            HotelDTO hotel = paymentHydrationOutputPort.getHotel(r.getEstablishmentId().toString());
            return new PaymentHydratedResponse(
                    r.getId().toString(),
                    r.getEstablishmentId(),
                    r.getClientId(),
                    r.getSourceType().toString(),
                    r.getSourceId(),
                    r.getSubtotal(),
                    r.getDiscount(),
                    r.getTotal(),
                    r.getMethod().toString(),
                    r.getStatus().toString(),
                    r.getCardNumber(),
                    promotionRestMapper.toResponse(r.getPromotionApplied()),
                    hotel,
                    null
                    );
        }
    }

    public List<PaymentHydratedResponse> toResponseList(List<Payment> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
