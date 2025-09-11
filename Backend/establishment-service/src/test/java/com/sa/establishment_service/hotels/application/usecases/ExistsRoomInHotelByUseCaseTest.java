package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

class ExistsRoomInHotelByUseCaseTest {

    private static class FakeExistsRoomInHotelByIdOutputPort implements ExistsRoomInHotelByIdOutputPort {
        boolean exists;
        @Override
        public boolean existsRoomInHotel(String hotelId, String roomId) { return exists; }
    }

    private FakeExistsRoomInHotelByIdOutputPort existsRoomInHotelByIdOutputPort;
    private ExistsRoomInHotelByUseCase useCase;

    @BeforeEach
    void setUp() {
        existsRoomInHotelByIdOutputPort = new FakeExistsRoomInHotelByIdOutputPort();
        useCase = new ExistsRoomInHotelByUseCase(existsRoomInHotelByIdOutputPort);
    }

    @Test
    void handle_doesNothing_whenRoomBelongsToHotel() {
        existsRoomInHotelByIdOutputPort.exists = true;
        assertDoesNotThrow(() -> useCase.handle("hid", "rid"));
    }

    @Test
    void handle_throwsNotFound_whenRoomNotInHotel() {
        existsRoomInHotelByIdOutputPort.exists = false;
        assertThrows(NotFoundException.class, () -> useCase.handle("hid", "rid"));
    }
}
