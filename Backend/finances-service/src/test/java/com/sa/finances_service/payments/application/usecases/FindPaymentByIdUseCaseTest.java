package com.sa.finances_service.payments.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.payments.application.outputports.FindPaymentByIdOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.shared.exceptions.NotFoundException;

class FindPaymentByIdUseCaseTest {

    private static class FakeFindPaymentByIdOutputPort implements FindPaymentByIdOutputPort {
        String expectedId;
        Optional<Payment> toReturn = Optional.empty();
        @Override
        public Optional<Payment> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeFindPaymentByIdOutputPort findPaymentByIdOutputPort;
    private FindPaymentByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findPaymentByIdOutputPort = new FakeFindPaymentByIdOutputPort();
        useCase = new FindPaymentByIdUseCase(findPaymentByIdOutputPort);
    }

    @Test
    void handle_returnsPayment_whenFound() throws Exception {
        String id = UUID.randomUUID().toString();
        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), SourceTypeEnum.ORDER, UUID.randomUUID(), new BigDecimal("10.00"), PaymentMethodEnum.CASH, null, LocalDate.now());
        findPaymentByIdOutputPort.expectedId = id;
        findPaymentByIdOutputPort.toReturn = Optional.of(payment);

        Payment result = useCase.handle(id);
        assertEquals(payment, result);
    }

    @Test
    void handle_throwsNotFound_whenMissing() {
        String id = UUID.randomUUID().toString();
        findPaymentByIdOutputPort.expectedId = id;
        findPaymentByIdOutputPort.toReturn = Optional.empty();
        assertThrows(NotFoundException.class, () -> useCase.handle(id));
    }
}

