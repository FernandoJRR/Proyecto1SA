package com.sa.client_service.clients.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import com.sa.client_service.clients.application.dtos.CreateClientDTO;
import com.sa.client_service.clients.application.outputports.CreateClientOutputPort;
import com.sa.client_service.clients.application.outputports.FindClientByEmailOrCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.DuplicatedEntryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateClientUseCaseTest {

    @Mock
    private CreateClientOutputPort createClientOutputPort;

    @Mock
    private FindClientByEmailOrCuiOutputPort findClientByEmailOrCuiOutputPort;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_whenNoExistingClient_createsAndReturnsClient() throws DuplicatedEntryException {
        // Arrange
        CreateClientDTO dto = new CreateClientDTO("Jane", "Doe", "jane@example.com", "12345678");

        when(findClientByEmailOrCuiOutputPort.findByEmailOrCui(dto.getEmail(), dto.getCui()))
            .thenReturn(Optional.empty());

        // Forward the same client instance that the use case constructs
        when(createClientOutputPort.createClient(any(Client.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = createClientUseCase.handle(dto);

        // Assert: Compare field-by-field equality (not identity)
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(new Client(result.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getCui()));
    }

    @Test
    void handle_whenClientExists_throwsDuplicatedEntryException() {
        // Arrange
        CreateClientDTO dto = new CreateClientDTO("John", "Smith", "john@example.com", "87654321");

        Client existing = new Client(null, dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getCui());
        when(findClientByEmailOrCuiOutputPort.findByEmailOrCui(dto.getEmail(), dto.getCui()))
            .thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> createClientUseCase.handle(dto));
    }
}