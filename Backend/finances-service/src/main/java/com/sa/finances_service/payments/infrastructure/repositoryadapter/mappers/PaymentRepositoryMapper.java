package com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentRepositoryMapper {
    public Payment toDomain(PaymentEntity paymentEntity);
    public List<Payment> toDomain(List<PaymentEntity> paymentEntity);
    public PaymentEntity toEntity(Payment payment);
}
