package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

class ExistsRoomByIdUseCaseTest {

    private static class FakeExistsRoomByIdOutputPort implements ExistsRoomByIdOutputPort {
        boolean exists;
        @Override
        public boolean existsById(String roomId) { return exists; }
    }

    private FakeExistsRoomByIdOutputPort existsRoomByIdOutputPort;
    private ExistsRoomByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        existsRoomByIdOutputPort = new FakeExistsRoomByIdOutputPort();
        useCase = new ExistsRoomByIdUseCase(existsRoomByIdOutputPort);
    }

    @Test
    void handle_doesNothing_whenRoomExists() {
        existsRoomByIdOutputPort.exists = true;
        assertDoesNotThrow(() -> useCase.handle("rid"));
    }

    @Test
    void handle_throwsNotFound_whenRoomMissing() {
        existsRoomByIdOutputPort.exists = false;
        assertThrows(NotFoundException.class, () -> useCase.handle("rid"));
    }
}
