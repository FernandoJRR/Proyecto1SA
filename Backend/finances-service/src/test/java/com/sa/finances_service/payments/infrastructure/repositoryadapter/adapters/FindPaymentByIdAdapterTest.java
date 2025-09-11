package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;

class FindPaymentByIdAdapterTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentRepositoryMapper paymentRepositoryMapper;
    private FindPaymentByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPaymentByIdAdapter(paymentRepository, paymentRepositoryMapper);
    }

    @Test
    void findById_mapsWhenPresent() {
        String id = UUID.randomUUID().toString();
        PaymentEntity entity = new PaymentEntity();
        Payment domain = new Payment();
        when(paymentRepository.findById(id)).thenReturn(Optional.of(entity));
        when(paymentRepositoryMapper.toDomain(entity)).thenReturn(domain);

        var result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findById_emptyWhenMissing() {
        String id = UUID.randomUUID().toString();
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());
        var result = adapter.findById(id);
        assertTrue(result.isEmpty());
    }
}

