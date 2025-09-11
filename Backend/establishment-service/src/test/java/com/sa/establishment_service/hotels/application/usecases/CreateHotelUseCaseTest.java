package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.CreateHotelDTO;
import com.sa.establishment_service.hotels.application.outputports.CreateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;

class CreateHotelUseCaseTest {

    private static class FakeCreateHotelOutputPort implements CreateHotelOutputPort {
        Hotel lastArg;
        Hotel toReturn;
        @Override
        public Hotel createHotel(Hotel hotel) {
            this.lastArg = hotel;
            return toReturn != null ? toReturn : hotel;
        }
    }

    private FakeCreateHotelOutputPort createHotelOutputPort;
    private CreateHotelUseCase useCase;

    @BeforeEach
    void setUp() {
        createHotelOutputPort = new FakeCreateHotelOutputPort();
        useCase = new CreateHotelUseCase(createHotelOutputPort);
    }

    @Test
    void handle_createsHotelAndDelegatesToOutputPort() {
        CreateHotelDTO dto = new CreateHotelDTO();
        dto.setName("Hotel Central");
        dto.setAddress("Main St 123");
        dto.setMaintenanceCostPerWeek(new BigDecimal("1200.50"));

        Hotel persisted = Hotel.create(dto.getName(), dto.getAddress(), dto.getMaintenanceCostPerWeek());
        createHotelOutputPort.toReturn = persisted;

        Hotel result = useCase.handle(dto);

        assertEquals(persisted, result);
        assertEquals("Hotel Central", createHotelOutputPort.lastArg.getName());
        assertEquals("Main St 123", createHotelOutputPort.lastArg.getAddress());
        assertEquals(new BigDecimal("1200.50"), createHotelOutputPort.lastArg.getMaintenanceCostPerWeek());
    }
}
