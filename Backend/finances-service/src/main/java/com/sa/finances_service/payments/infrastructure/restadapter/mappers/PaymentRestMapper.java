package com.sa.finances_service.payments.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.CreatePaymentRequest;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentRestMapper {
    public CreatePaymentDTO toDTO(CreatePaymentRequest paymentRequest);
    public PaymentResponse toResponse(Payment payment);
}
