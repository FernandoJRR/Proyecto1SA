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

class FindClientByCuiAdapterTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientRepositoryMapper clientRepositoryMapper;

    @InjectMocks
    private FindClientByCuiAdapter adapter;

    private String cui = "CUI123";
    private UUID clientId = UUID.randomUUID();
    private ClientEntity foundEntity;
    private Client domainClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare a ClientEntity that repository returns
        foundEntity = new ClientEntity();
        foundEntity.setId(clientId.toString());
        foundEntity.setFirstName("John");
        foundEntity.setLastName("Doe");
        foundEntity.setEmail("john.doe@example.com");
        foundEntity.setCui(cui);

        // Prepare the domain version
        domainClient = new Client(
            clientId,
            foundEntity.getFirstName(),
            foundEntity.getLastName(),
            foundEntity.getEmail(),
            foundEntity.getCui()
        );

        // Mock the repository and mapper behavior
        when(clientRepository.findFirstByCui(cui)).thenReturn(Optional.of(foundEntity));
        when(clientRepositoryMapper.toDomain(foundEntity)).thenReturn(domainClient);
    }

    @Test
    void findByCui_whenClientExists_returnsOptionalOfClient() {
        Optional<Client> result = adapter.findByCui(cui);

        assertTrue(result.isPresent());
        assertEquals(domainClient, result.get());

        verify(clientRepository).findFirstByCui(cui);
        verify(clientRepositoryMapper).toDomain(foundEntity);
    }

    @Test
    void findByCui_whenClientDoesNotExist_returnsEmptyOptional() {
        String otherCui = "NOT_FOUND";
        when(clientRepository.findFirstByCui(otherCui)).thenReturn(Optional.empty());

        Optional<Client> result = adapter.findByCui(otherCui);

        assertFalse(result.isPresent());
        verify(clientRepository).findFirstByCui(otherCui);
        verify(clientRepositoryMapper, never()).toDomain(any());
    }
}