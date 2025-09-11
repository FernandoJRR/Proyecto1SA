package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.finances_service.payments.application.outputports.CreatePaymentOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreatePaymentAdapter implements CreatePaymentOutputPort {

    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryMapper paymentRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Payment createPayment(Payment payment) {
        PaymentEntity paymentEntity = paymentRepositoryMapper.toEntity(payment);
        PaymentEntity createdEntity = paymentRepository.save(paymentEntity);
        return paymentRepositoryMapper.toDomain(createdEntity);
    }

}
