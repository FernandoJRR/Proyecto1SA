package com.sa.finances_service.payments.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.application.outputports.CreatePaymentOutputPort;
import com.sa.finances_service.payments.application.outputports.FindPaymentByIdOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.promotions.application.outputports.FindPromotionByIdOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.NotFoundException;

class CreatePaymentUseCaseTest {

    private static class FakeCreatePaymentOutputPort implements CreatePaymentOutputPort {
        Payment lastArg;
        Payment toReturn;
        @Override
        public Payment createPayment(Payment payment) {
            lastArg = payment;
            return toReturn != null ? toReturn : payment;
        }
    }

    private static class FakeFindPromotionByIdOutputPort implements FindPromotionByIdOutputPort {
        String expectedId;
        Optional<Promotion> toReturn = Optional.empty();
        @Override
        public Optional<Promotion> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeCreatePaymentOutputPort createPaymentOutputPort;
    private FakeFindPromotionByIdOutputPort findPromotionByIdOutputPort;
    private CreatePaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        createPaymentOutputPort = new FakeCreatePaymentOutputPort();
        findPromotionByIdOutputPort = new FakeFindPromotionByIdOutputPort();
        useCase = new CreatePaymentUseCase(createPaymentOutputPort, findPromotionByIdOutputPort);
    }

    @Test
    void handle_withoutPromotion_createsPaymentWithTotals() throws Exception {
        UUID establishmentId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();
        LocalDate paidAt = LocalDate.now();

        CreatePaymentDTO dto = new CreatePaymentDTO(
            establishmentId,
            clientId,
            "ORDER",
            sourceId,
            "CASH",
            new BigDecimal("100.00"),
            null,
            null,
            paidAt
        );

        Payment result = useCase.handle(dto);

        assertEquals(establishmentId, createPaymentOutputPort.lastArg.getEstablishmentId());
        assertEquals(clientId, createPaymentOutputPort.lastArg.getClientId());
        assertEquals(new BigDecimal("100.00"), createPaymentOutputPort.lastArg.getSubtotal());
        assertEquals(new BigDecimal("100.00"), createPaymentOutputPort.lastArg.getTotal());
        assertEquals(paidAt, createPaymentOutputPort.lastArg.getPaidAt());
        assertEquals(createPaymentOutputPort.lastArg, result);
    }

    @Test
    void handle_withPromotion_appliesDiscount() throws Exception {
        UUID establishmentId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();
        LocalDate paidAt = LocalDate.now();
        UUID promotionId = UUID.randomUUID();

        CreatePaymentDTO dto = new CreatePaymentDTO(
            establishmentId, clientId, "ORDER", sourceId, "CARD",
            new BigDecimal("200.00"), "1234", promotionId.toString(), paidAt
        );

        Promotion promo = Promotion.create(new BigDecimal("10"), paidAt.minusDays(1), paidAt.plusDays(1),
            "Promo 10%", establishmentId, EstablishmentTypeEnum.RESTAURANT, null, PromotionType.CLIENT_MOST_FREQUENT);
        findPromotionByIdOutputPort.expectedId = promotionId.toString();
        findPromotionByIdOutputPort.toReturn = Optional.of(promo);

        Payment result = useCase.handle(dto);

        assertNotNull(createPaymentOutputPort.lastArg.getPromotionApplied());
        // 10% of 200 = 20 discount, total = 180
        assertEquals(new BigDecimal("180.000"), createPaymentOutputPort.lastArg.getTotal());
        assertEquals(createPaymentOutputPort.lastArg, result);
    }

    @Test
    void handle_withPromotionMissing_throwsNotFound() {
        UUID establishmentId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();
        LocalDate paidAt = LocalDate.now();
        UUID promotionId = UUID.randomUUID();

        CreatePaymentDTO dto = new CreatePaymentDTO(
            establishmentId, clientId, "ORDER", sourceId, "CARD",
            new BigDecimal("200.00"), "1234", promotionId.toString(), paidAt
        );

        findPromotionByIdOutputPort.expectedId = promotionId.toString();
        findPromotionByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }
}
