package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

class FindHotelByIdUseCaseTest {

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        UUID expectedId;
        Optional<Hotel> toReturn = Optional.empty();
        @Override
        public Optional<Hotel> findHotelById(UUID id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FindHotelByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        useCase = new FindHotelByIdUseCase(findHotelByIdOutputPort);
    }

    @Test
    void handle_returnsHotel_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        Hotel hotel = Hotel.create("H", "A", new BigDecimal("10"));
        findHotelByIdOutputPort.expectedId = id;
        findHotelByIdOutputPort.toReturn = Optional.of(hotel);

        Hotel result = useCase.handle(id.toString());

        assertEquals(hotel, result);
    }

    @Test
    void handle_throwsNotFound_whenEmpty() {
        UUID id = UUID.randomUUID();
        findHotelByIdOutputPort.expectedId = id;
        findHotelByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () -> useCase.handle(id.toString()));
    }
}
