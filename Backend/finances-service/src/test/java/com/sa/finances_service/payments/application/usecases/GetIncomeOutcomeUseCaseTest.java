package com.sa.finances_service.payments.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.application.outputports.GetOutcomeByDateOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.reports.application.dtos.IncomeOutcomeDTO;
import com.sa.finances_service.reports.application.dtos.OutcomeDTO;
import com.sa.shared.exceptions.NotFoundException;

class GetIncomeOutcomeUseCaseTest {

    private static class FakeFindPaymentsOutputPort implements FindPaymentsOutputPort {
        FindPaymentsDTO lastFilter;
        List<Payment> toReturn = List.of();
        @Override
        public List<Payment> findAll(FindPaymentsDTO dto) {
            lastFilter = dto;
            return toReturn;
        }
    }

    private static class FakeGetOutcomeByDateOutputPort implements GetOutcomeByDateOutputPort {
        LocalDate lastFrom;
        LocalDate lastTo;
        Optional<OutcomeDTO> toReturn = Optional.empty();
        @Override
        public Optional<OutcomeDTO> getByDate(LocalDate fromDate, LocalDate toDate) {
            lastFrom = fromDate; lastTo = toDate;
            return toReturn;
        }
    }

    private FakeFindPaymentsOutputPort findPaymentsOutputPort;
    private FakeGetOutcomeByDateOutputPort getOutcomeByDateOutputPort;
    private GetIncomeOutcomeUseCase useCase;

    @BeforeEach
    void setUp() {
        findPaymentsOutputPort = new FakeFindPaymentsOutputPort();
        getOutcomeByDateOutputPort = new FakeGetOutcomeByDateOutputPort();
        useCase = new GetIncomeOutcomeUseCase(findPaymentsOutputPort, getOutcomeByDateOutputPort);
    }

    @Test
    void handle_aggregatesIncomeAndOutcome_whenOutcomePresent() throws Exception {
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        Payment p1 = Payment.create(UUID.randomUUID(), UUID.randomUUID(), SourceTypeEnum.ORDER, UUID.randomUUID(), new BigDecimal("50.00"), PaymentMethodEnum.CASH, null, to);
        Payment p2 = Payment.create(UUID.randomUUID(), UUID.randomUUID(), SourceTypeEnum.RESERVATION, UUID.randomUUID(), new BigDecimal("100.00"), PaymentMethodEnum.CARD, "1234", to);
        findPaymentsOutputPort.toReturn = List.of(p1, p2);

        OutcomeDTO outcome = new OutcomeDTO(new BigDecimal("30.00"), new BigDecimal("20.00"));
        getOutcomeByDateOutputPort.toReturn = Optional.of(outcome);

        IncomeOutcomeDTO result = useCase.handle(from, to);

        assertEquals(new BigDecimal("150.00"), result.getTotalIncome());
        assertEquals(new BigDecimal("50.00"), result.getTotalOutcome());
        assertEquals(outcome, result.getOutcome());

        // filter propagation
        assertEquals(from, findPaymentsOutputPort.lastFilter.getFromDate());
        assertEquals(to, findPaymentsOutputPort.lastFilter.getToDate());
    }

    @Test
    void handle_throwsNotFound_whenOutcomeMissing() {
        LocalDate from = LocalDate.now().minusDays(3);
        LocalDate to = LocalDate.now();
        getOutcomeByDateOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(from, to));
    }
}

