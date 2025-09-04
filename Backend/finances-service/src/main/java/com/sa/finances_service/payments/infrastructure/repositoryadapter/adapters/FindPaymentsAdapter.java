package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import java.util.List;

import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPaymentsAdapter implements FindPaymentsOutputPort {

    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryMapper paymentRepositoryMapper;

    @Override
    public List<Payment> findAll() {
        List<PaymentEntity> result = paymentRepository.findAll();
        return paymentRepositoryMapper.toDomain(result);
    }

}
