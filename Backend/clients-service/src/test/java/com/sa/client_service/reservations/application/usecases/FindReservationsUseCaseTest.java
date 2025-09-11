package com.sa.client_service.reservations.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.reservations.application.dtos.FindReservationsDTO;
import com.sa.client_service.reservations.application.outputports.FindReservationsOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.InvalidParameterException;

@ExtendWith(MockitoExtension.class)
public class FindReservationsUseCaseTest {

    @Mock private FindReservationsOutputPort findReservationsOutputPort;

    private FindReservationsUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new FindReservationsUseCase(findReservationsOutputPort);
    }

    @Test
    void givenValidDateRange_callsOutputPortAndReturnsList() throws Exception {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        String cui = "1234567890123";
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);

        FindReservationsDTO dto = FindReservationsDTO.builder()
            .hotelId(hotelId)
            .roomId(roomId)
            .clientCui(cui)
            .startDate(start)
            .endDate(end)
            .build();

        List<Reservation> list = List.of(new Reservation(UUID.randomUUID(), null, null, null, null, null, null, null, null, null));
        when(findReservationsOutputPort.findReservations(hotelId, roomId, cui, start, end)).thenReturn(list);

        List<Reservation> result = useCase.handle(dto);

        assertEquals(list, result);
        verify(findReservationsOutputPort, times(1)).findReservations(hotelId, roomId, cui, start, end);
    }

    @Test
    void givenStartAfterEnd_throwsInvalidParameterException() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);
        FindReservationsDTO dto = FindReservationsDTO.builder()
            .startDate(start)
            .endDate(end)
            .build();

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }
}

