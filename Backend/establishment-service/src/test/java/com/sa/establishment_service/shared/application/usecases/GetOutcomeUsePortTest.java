package com.sa.establishment_service.shared.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindAllHotelsOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.shared.application.dtos.OrderDTO;
import com.sa.establishment_service.shared.application.dtos.OutcomeDTO;
import com.sa.establishment_service.shared.application.outputports.FindOrdersByDateOutputPort;

class GetOutcomeUsePortTest {

    private static class FakeFindAllHotelsOutputPort implements FindAllHotelsOutputPort {
        List<Hotel> toReturn = List.of();
        @Override
        public List<Hotel> findAll() { return toReturn; }
    }

    private static class FakeFindOrdersByDateOutputPort implements FindOrdersByDateOutputPort {
        LocalDate lastFrom;
        LocalDate lastTo;
        List<OrderDTO> toReturn = List.of();
        @Override
        public List<OrderDTO> findOrdersByDate(LocalDate fromDate, LocalDate toDate) {
            lastFrom = fromDate; lastTo = toDate;
            return toReturn;
        }
    }

    private FakeFindAllHotelsOutputPort findAllHotelsOutputPort;
    private FakeFindOrdersByDateOutputPort findOrdersByDateOutputPort;
    private GetOutcomeUsePort useCase;

    @BeforeEach
    void setUp() {
        findAllHotelsOutputPort = new FakeFindAllHotelsOutputPort();
        findOrdersByDateOutputPort = new FakeFindOrdersByDateOutputPort();
        useCase = new GetOutcomeUsePort(findAllHotelsOutputPort, findOrdersByDateOutputPort);
    }

    @Test
    void handle_sumsHotelsAndOrdersWithWeeks() {
        // weeks between 2024-01-01 and 2024-01-14 (13 days) -> 2
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 14);

        findAllHotelsOutputPort.toReturn = List.of(
            Hotel.create("H1", "A1", new BigDecimal("100")),
            Hotel.create("H2", "A2", new BigDecimal("200"))
        );

        findOrdersByDateOutputPort.toReturn = List.of(
            new OrderDTO("o1", "c1", null, new BigDecimal("50"), new BigDecimal("50")),
            new OrderDTO("o2", "c2", null, new BigDecimal("150"), new BigDecimal("150"))
        );

        OutcomeDTO result = useCase.handle(from, to);

        // hotels: (100 + 200) * 2 = 600
        assertEquals(new BigDecimal(600), result.getTotalOutcomeHotels());
        // restaurants: (50 + 150) * 0.20 * 2 = 80.00
        assertEquals(new BigDecimal("80.0"), result.getTotalOutcomeRestaurants());
    }

    @Test
    void handle_zeroWeeks_returnsZeroOutcomes() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        findAllHotelsOutputPort.toReturn = List.of(Hotel.create("H", "A", new BigDecimal("999")));
        findOrdersByDateOutputPort.toReturn = List.of(new OrderDTO("o", "c", null, new BigDecimal("100"), new BigDecimal("100")));

        OutcomeDTO result = useCase.handle(day, day);

        assertEquals(BigInteger.ZERO, result.getTotalOutcomeHotels().toBigInteger());
        assertEquals(BigInteger.ZERO, result.getTotalOutcomeRestaurants().toBigInteger());
    }
}
