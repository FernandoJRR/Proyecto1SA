package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;

class CreatePaymentAdapterTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentRepositoryMapper paymentRepositoryMapper;

    private CreatePaymentAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreatePaymentAdapter(paymentRepository, paymentRepositoryMapper);
    }

    @Test
    void createPayment_mapsAndPersists_thenMapsBack() {
        Payment domain = Payment.create(
            UUID.randomUUID(), UUID.randomUUID(),
            SourceTypeEnum.ORDER, UUID.randomUUID(),
            new BigDecimal("99.99"), PaymentMethodEnum.CASH, null, LocalDate.now()
        );

        PaymentEntity entity = new PaymentEntity();
        PaymentEntity saved = new PaymentEntity();
        Payment mappedBack = domain;

        when(paymentRepositoryMapper.toEntity(domain)).thenReturn(entity);
        when(paymentRepository.save(entity)).thenReturn(saved);
        when(paymentRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Payment result = adapter.createPayment(domain);

        assertEquals(mappedBack, result);
        verify(paymentRepositoryMapper).toEntity(domain);
        verify(paymentRepository).save(entity);
        verify(paymentRepositoryMapper).toDomain(saved);
    }
}

