package com.sa.client_service.clients.application.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.application.outputports.FindPaymentsByClientOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class GetIncomeClientUseCaseTest {

    @Mock
    private FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Mock
    private FindPaymentsByClientOutputPort findPaymentsByClientOutputPort;

    @InjectMocks
    private GetIncomeClientUseCase useCase;

    private String clientCui;
    private String establishmentId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private UUID clientId;
    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientCui = "12345678";
        establishmentId = "est123";
        fromDate = LocalDate.of(2025, 9, 1);
        toDate = LocalDate.of(2025, 9, 30);
        clientId = UUID.randomUUID();
        client = new Client(clientId, "First", "Last", "email@example.com", clientCui);
    }

    @Test
    void handle_whenClientDoesNotExist_throwsNotFoundException() {
        when(findClientByCuiOutputPort.findByCui(clientCui)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
            useCase.handle(clientCui, establishmentId, fromDate, toDate));
        assertEquals("El cliente ingresado no existe", ex.getMessage());
    }

    @Test
    void handle_whenPaymentsFound_returnsListWithDescriptionSet() throws NotFoundException, InvalidParameterException {
        when(findClientByCuiOutputPort.findByCui(clientCui)).thenReturn(Optional.of(client));

        PaymentDTO p1 = new PaymentDTO(/* fill in parameters: id, clientId, etc. */);
        PaymentDTO p2 = new PaymentDTO(/* fill in parameters */);

        List<PaymentDTO> payments = List.of(p1, p2);

        when(findPaymentsByClientOutputPort.findByClient(clientId.toString(), establishmentId, fromDate, toDate))
            .thenReturn(payments);

        List<PaymentDTO> result = useCase.handle(clientCui, establishmentId, fromDate, toDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        // description should be "Consumo" for both
        for (PaymentDTO p : result) {
            assertEquals("Consumo", p.getDescription());
        }
        // also, original fields preserved
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
    }

    @Test
    void handle_whenEstablishmentIdNull_returnsListWithDescriptionSet() throws NotFoundException, InvalidParameterException {
        when(findClientByCuiOutputPort.findByCui(clientCui)).thenReturn(Optional.of(client));

        PaymentDTO p = new PaymentDTO(/* parameters */);

        when(findPaymentsByClientOutputPort.findByClient(clientId.toString(), null, fromDate, toDate))
            .thenReturn(List.of(p));

        List<PaymentDTO> result = useCase.handle(clientCui, null, fromDate, toDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Consumo", result.get(0).getDescription());
    }

    @Test
    void handle_whenFromDateAfterToDate_throwsInvalidParameterException() {
        LocalDate badFrom = LocalDate.of(2025, 10, 1);
        LocalDate badTo = LocalDate.of(2025, 9, 1);

        when(findClientByCuiOutputPort.findByCui(clientCui)).thenReturn(Optional.of(client));

        assertThrows(InvalidParameterException.class, () ->
            useCase.handle(clientCui, establishmentId, badFrom, badTo));
    }
}