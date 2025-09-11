package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;

class FindPaymentsAdapterTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentRepositoryMapper paymentRepositoryMapper;
    private FindPaymentsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPaymentsAdapter(paymentRepository, paymentRepositoryMapper);
    }

    @Test
    void findAll_buildsSpec_delegatesToRepo_andMaps() {
        FindPaymentsDTO filter = FindPaymentsDTO.builder()
            .clientId(UUID.randomUUID().toString())
            .establishmentId(UUID.randomUUID().toString())
            .method("CASH")
            .sourceType("ORDER")
            .fromDate(LocalDate.now().minusDays(10))
            .toDate(LocalDate.now())
            .build();

        PaymentEntity e1 = new PaymentEntity();
        PaymentEntity e2 = new PaymentEntity();
        when(paymentRepository.findAll(any(Specification.class))).thenReturn(List.of(e1, e2));

        Payment d1 = new Payment();
        Payment d2 = new Payment();
        when(paymentRepositoryMapper.toDomain(e1)).thenReturn(d1);
        when(paymentRepositoryMapper.toDomain(e2)).thenReturn(d2);

        List<Payment> result = adapter.findAll(filter);

        assertEquals(List.of(d1, d2), result);
        verify(paymentRepository).findAll(any(Specification.class));
    }
}

