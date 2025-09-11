package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.application.outputports.CreatePromotionOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.InvalidParameterException;

class CreatePromotionUseCaseTest {

    private static class FakeCreatePromotionOutputPort implements CreatePromotionOutputPort {
        Promotion lastArg;
        Promotion toReturn;
        @Override
        public Promotion createPromotion(Promotion promotion) {
            lastArg = promotion;
            return toReturn != null ? toReturn : promotion;
        }
    }

    private FakeCreatePromotionOutputPort createPromotionOutputPort;
    private CreatePromotionUseCase useCase;

    @BeforeEach
    void setUp() {
        createPromotionOutputPort = new FakeCreatePromotionOutputPort();
        useCase = new CreatePromotionUseCase(createPromotionOutputPort);
    }

    @Test
    void handle_createsPromotion_whenValid() throws Exception {
        UUID estId = UUID.randomUUID();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);
        CreatePromotionDTO dto = new CreatePromotionDTO(
            new BigDecimal("15"), start, end,
            PromotionType.CLIENT_MOST_FREQUENT.name(),
            estId.toString(), EstablishmentTypeEnum.HOTEL.name(),
            "Promo CF", 3
        );

        Promotion result = useCase.handle(dto);

        assertEquals(new BigDecimal("15"), createPromotionOutputPort.lastArg.getPercentage());
        assertEquals(start, createPromotionOutputPort.lastArg.getStartDate());
        assertEquals(end, createPromotionOutputPort.lastArg.getEndDate());
        assertEquals(estId, createPromotionOutputPort.lastArg.getEstablishmentId());
        assertEquals(EstablishmentTypeEnum.HOTEL, createPromotionOutputPort.lastArg.getEstablishmentType());
        assertEquals(PromotionType.CLIENT_MOST_FREQUENT, createPromotionOutputPort.lastArg.getPromotionType());
        assertEquals(3, createPromotionOutputPort.lastArg.getTopCount());
        assertEquals(createPromotionOutputPort.lastArg, result);
    }

    @Test
    void handle_throwsInvalid_whenStartAfterEnd() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);
        CreatePromotionDTO dto = new CreatePromotionDTO(new BigDecimal("10"), start, end,
            PromotionType.CLIENT_MOST_FREQUENT.name(), UUID.randomUUID().toString(), EstablishmentTypeEnum.HOTEL.name(), "X", 2);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_throwsInvalid_whenUnknownPromotionType() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        CreatePromotionDTO dto = new CreatePromotionDTO(new BigDecimal("10"), start, end,
            "UNKNOWN", UUID.randomUUID().toString(), EstablishmentTypeEnum.HOTEL.name(), "X", 2);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_throwsInvalid_whenUnknownEstablishmentType() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        CreatePromotionDTO dto = new CreatePromotionDTO(new BigDecimal("10"), start, end,
            PromotionType.CLIENT_MOST_FREQUENT.name(), UUID.randomUUID().toString(), "UNKNOWN", "X", 2);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_throwsInvalid_whenDishPromoForHotel() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        CreatePromotionDTO dto = new CreatePromotionDTO(new BigDecimal("10"), start, end,
            PromotionType.DISH_MOST_POPULAR.name(), UUID.randomUUID().toString(), EstablishmentTypeEnum.HOTEL.name(), "X", 2);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_throwsInvalid_whenRoomPromoForRestaurant() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        CreatePromotionDTO dto = new CreatePromotionDTO(new BigDecimal("10"), start, end,
            PromotionType.ROOM_MOST_POPULAR.name(), UUID.randomUUID().toString(), EstablishmentTypeEnum.RESTAURANT.name(), "X", 2);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }
}

