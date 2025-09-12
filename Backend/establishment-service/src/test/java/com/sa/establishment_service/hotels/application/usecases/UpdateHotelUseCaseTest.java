package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.UpdateHotelDTO;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.UpdateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

class UpdateHotelUseCaseTest {

    private static class FakeFindHotelByIdOutputPort implements FindHotelByIdOutputPort {
        UUID expectedId;
        UUID lastCalledId;
        Optional<Hotel> toReturn = Optional.empty();
        @Override
        public Optional<Hotel> findHotelById(UUID id) {
            lastCalledId = id;
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeUpdateHotelOutputPort implements UpdateHotelOutputPort {
        Hotel lastArg;
        Hotel toReturn;
        @Override
        public Hotel updateHotel(Hotel hotel) {
            lastArg = hotel;
            return toReturn != null ? toReturn : hotel;
        }
    }

    private FakeFindHotelByIdOutputPort findHotelByIdOutputPort;
    private FakeUpdateHotelOutputPort updateHotelOutputPort;
    private UpdateHotelUseCase useCase;

    @BeforeEach
    void setUp() {
        findHotelByIdOutputPort = new FakeFindHotelByIdOutputPort();
        updateHotelOutputPort = new FakeUpdateHotelOutputPort();
        useCase = new UpdateHotelUseCase(findHotelByIdOutputPort, updateHotelOutputPort);
    }

    @Test
    void handle_updatesFields_andDelegatesToOutputPort() throws Exception {
        UUID id = UUID.randomUUID();
        Hotel existing = new Hotel(id, "Old Name", "Old Addr", new BigDecimal("10.00"));
        findHotelByIdOutputPort.expectedId = id;
        findHotelByIdOutputPort.toReturn = Optional.of(existing);

        UpdateHotelDTO dto = new UpdateHotelDTO();
        dto.setName("New Name");
        dto.setAddress("New Addr");
        dto.setMaintenanceCostPerWeek(new BigDecimal("22.50"));

        Hotel returned = new Hotel(id, dto.getName(), dto.getAddress(), dto.getMaintenanceCostPerWeek());
        updateHotelOutputPort.toReturn = returned;

        Hotel result = useCase.handle(id.toString(), dto);

        assertEquals(id, findHotelByIdOutputPort.lastCalledId);
        assertEquals("New Name", updateHotelOutputPort.lastArg.getName());
        assertEquals("New Addr", updateHotelOutputPort.lastArg.getAddress());
        assertEquals(new BigDecimal("22.50"), updateHotelOutputPort.lastArg.getMaintenanceCostPerWeek());
        assertEquals(returned, result);
    }

    @Test
    void handle_throwsNotFound_whenHotelDoesNotExist() {
        UUID id = UUID.randomUUID();
        findHotelByIdOutputPort.expectedId = id;
        findHotelByIdOutputPort.toReturn = Optional.empty();

        UpdateHotelDTO dto = new UpdateHotelDTO();
        dto.setName("N");
        dto.setAddress("A");
        dto.setMaintenanceCostPerWeek(new BigDecimal("1.00"));

        assertThrows(NotFoundException.class, () -> useCase.handle(id.toString(), dto));
    }
}

