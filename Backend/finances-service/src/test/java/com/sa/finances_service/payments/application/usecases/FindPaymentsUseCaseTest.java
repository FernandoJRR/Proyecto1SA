package com.sa.finances_service.payments.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;

class FindPaymentsUseCaseTest {

    private static class FakeFindPaymentsOutputPort implements FindPaymentsOutputPort {
        FindPaymentsDTO lastFilter;
        List<Payment> toReturn = List.of();
        @Override
        public List<Payment> findAll(FindPaymentsDTO dto) {
            lastFilter = dto;
            return toReturn;
        }
    }

    private FakeFindPaymentsOutputPort findPaymentsOutputPort;
    private FindPaymentsUseCase useCase;

    @BeforeEach
    void setUp() {
        findPaymentsOutputPort = new FakeFindPaymentsOutputPort();
        useCase = new FindPaymentsUseCase(findPaymentsOutputPort);
    }

    @Test
    void handle_returnsListAndForwardsFilter() {
        FindPaymentsDTO filter = FindPaymentsDTO.builder()
            .clientId(UUID.randomUUID().toString())
            .fromDate(LocalDate.now().minusDays(10))
            .toDate(LocalDate.now())
            .method("CASH")
            .build();

        List<Payment> payments = List.of(
            Payment.create(UUID.randomUUID(), UUID.randomUUID(), SourceTypeEnum.ORDER, UUID.randomUUID(), new BigDecimal("10.00"), PaymentMethodEnum.CASH, null, LocalDate.now())
        );
        findPaymentsOutputPort.toReturn = payments;

        List<Payment> result = useCase.handle(filter);

        assertEquals(payments, result);
        assertEquals(filter, findPaymentsOutputPort.lastFilter);
    }
}

