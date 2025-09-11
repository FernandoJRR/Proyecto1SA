package com.sa.client_service.reservations.application.usecases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.application.outputports.FindReservationByIdOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class FindReservationByIdUseCaseTest {

    @Mock private FindReservationByIdOutputPort findReservationByIdOutputPort;

    private FindReservationByIdUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new FindReservationByIdUseCase(findReservationByIdOutputPort);
    }

    @Test
    void givenExistingReservation_returnsIt() throws Exception {
        String id = UUID.randomUUID().toString();
        Reservation r = new Reservation(UUID.randomUUID(), null, null, null, null, null, null, null, null, null);
        when(findReservationByIdOutputPort.findById(id)).thenReturn(Optional.of(r));

        Reservation result = useCase.handle(id);
        assertSame(r, result);
    }

    @Test
    void givenMissingReservation_throwsNotFound() {
        String id = UUID.randomUUID().toString();
        when(findReservationByIdOutputPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.handle(id));
    }
}

