package com.sa.client_service.clients.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.repositoryadapter.mappers.ClientRepositoryMapper;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindClientByEmailOrCuiAdapterTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientRepositoryMapper clientRepositoryMapper;

    @InjectMocks
    private FindClientByEmailOrCuiAdapter adapter;

    private String email = "test@example.com";
    private String cui = "CUI123";
    private UUID clientId;
    private ClientEntity foundEntity;
    private Client domainClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clientId = UUID.randomUUID();
        foundEntity = new ClientEntity();
        foundEntity.setId(clientId.toString());
        foundEntity.setFirstName("Jane");
        foundEntity.setLastName("Doe");
        foundEntity.setEmail(email);
        foundEntity.setCui(cui);

        domainClient = new Client(
            clientId,
            foundEntity.getFirstName(),
            foundEntity.getLastName(),
            foundEntity.getEmail(),
            foundEntity.getCui()
        );

        when(clientRepository.findFirstByEmailOrCui(email, cui))
            .thenReturn(Optional.of(foundEntity));
        when(clientRepositoryMapper.toDomain(foundEntity))
            .thenReturn(domainClient);
    }

    @Test
    void findByEmailOrCui_whenClientExists_returnsOptionalOfClient() {
        Optional<Client> result = adapter.findByEmailOrCui(email, cui);

        assertTrue(result.isPresent());
        assertEquals(domainClient, result.get());

        verify(clientRepository).findFirstByEmailOrCui(email, cui);
        verify(clientRepositoryMapper).toDomain(foundEntity);
    }

    @Test
    void findByEmailOrCui_whenClientDoesNotExist_returnsEmptyOptional() {
        String otherEmail = "other@example.com";
        String otherCui = "OTHERCUI";
        when(clientRepository.findFirstByEmailOrCui(otherEmail, otherCui))
            .thenReturn(Optional.empty());

        Optional<Client> result = adapter.findByEmailOrCui(otherEmail, otherCui);

        assertFalse(result.isPresent());
        verify(clientRepository).findFirstByEmailOrCui(otherEmail, otherCui);
        verify(clientRepositoryMapper, never()).toDomain(any());
    }
}