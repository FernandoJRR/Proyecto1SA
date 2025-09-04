package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.finances_service.payments.application.outputports.FindPaymentByIdOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPaymentByIdAdapter implements FindPaymentByIdOutputPort {

    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryMapper paymentRepositoryMapper;

    @Override
    public Optional<Payment> findById(String id) {
        return paymentRepository.findById(id)
            .map(paymentRepositoryMapper::toDomain);
    }

}
