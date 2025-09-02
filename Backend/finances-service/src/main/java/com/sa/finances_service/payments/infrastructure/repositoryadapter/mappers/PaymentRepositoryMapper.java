package com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentRepositoryMapper {
    public Payment toDomain(PaymentEntity paymentEntity);
    public PaymentEntity toEntity(Payment payment);
}
