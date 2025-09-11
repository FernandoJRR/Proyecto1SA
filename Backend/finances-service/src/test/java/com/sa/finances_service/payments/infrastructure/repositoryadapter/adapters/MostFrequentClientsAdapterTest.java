package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;

class MostFrequentClientsAdapterTest {

    @Mock private PaymentRepository paymentRepository;
    private MostFrequentClientsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new MostFrequentClientsAdapter(paymentRepository);
    }

    @Test
    void findMostFrequent_usesLimitAndMapsUUIDs() {
        String c1 = UUID.randomUUID().toString();
        String c2 = UUID.randomUUID().toString();
        when(paymentRepository.findTopClientsByPaymentCount(any(PageRequest.class)))
            .thenReturn(List.of(c1, c2));

        List<UUID> result = adapter.findMostFrequent(3);

        assertEquals(2, result.size());
        assertEquals(UUID.fromString(c1), result.get(0));
        assertEquals(UUID.fromString(c2), result.get(1));

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(paymentRepository).findTopClientsByPaymentCount(captor.capture());
        assertEquals(3, captor.getValue().getPageSize());
        assertEquals(0, captor.getValue().getPageNumber());
    }
}

