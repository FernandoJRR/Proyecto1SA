package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindAllHotelsOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;

class FindAllHotelsUseCaseTest {

    private static class FakeFindAllHotelsOutputPort implements FindAllHotelsOutputPort {
        List<Hotel> toReturn = List.of();
        @Override
        public List<Hotel> findAll() { return toReturn; }
    }

    private FakeFindAllHotelsOutputPort findAllHotelsOutputPort;
    private FindAllHotelsUseCase useCase;

    @BeforeEach
    void setUp() {
        findAllHotelsOutputPort = new FakeFindAllHotelsOutputPort();
        useCase = new FindAllHotelsUseCase(findAllHotelsOutputPort);
    }

    @Test
    void handle_returnsListFromOutputPort() {
        List<Hotel> hotels = List.of(
            Hotel.create("H1", "A1", new BigDecimal("10")),
            Hotel.create("H2", "A2", new BigDecimal("20"))
        );
        findAllHotelsOutputPort.toReturn = hotels;

        List<Hotel> result = useCase.handle();

        assertEquals(hotels, result);
        assertEquals(2, result.size());
    }
}
