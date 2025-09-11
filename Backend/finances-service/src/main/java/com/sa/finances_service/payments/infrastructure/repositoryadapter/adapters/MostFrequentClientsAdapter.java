package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.finances_service.promotions.application.outputports.MostFrequentClientsOutputPort;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MostFrequentClientsAdapter implements MostFrequentClientsOutputPort {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UUID> findMostFrequent(Integer limit) {
        PageRequest pageableLimit = PageRequest.of(0, limit);
        List<String> result = paymentRepository.findTopClientsByPaymentCount(pageableLimit);
        return result.stream().map(UUID::fromString).toList();
    }

}
