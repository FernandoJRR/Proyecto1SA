package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

class ExistsHotelByIdUseCaseTest {

    private static class FakeExistsHotelByIdOutputPort implements ExistsHotelByIdOutputPort {
        boolean exists;
        @Override
        public boolean existsById(String id) { return exists; }
    }

    private FakeExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private ExistsHotelByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        existsHotelByIdOutputPort = new FakeExistsHotelByIdOutputPort();
        useCase = new ExistsHotelByIdUseCase(existsHotelByIdOutputPort);
    }

    @Test
    void handle_doesNothing_whenHotelExists() {
        existsHotelByIdOutputPort.exists = true;
        assertDoesNotThrow(() -> useCase.handle("abc"));
    }

    @Test
    void handle_throwsNotFound_whenHotelDoesNotExist() {
        existsHotelByIdOutputPort.exists = false;
        assertThrows(NotFoundException.class, () -> useCase.handle("abc"));
    }
}
