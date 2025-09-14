package com.sa.client_service.clients.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.repositoryadapter.mappers.ClientRepositoryMapper;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CreateClientAdapterTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientRepositoryMapper clientRepositoryMapper;

    @InjectMocks
    private CreateClientAdapter adapter;

    private Client domainClient;
    private ClientEntity entityToSave;
    private ClientEntity savedEntity;
    private Client domainFromSaved;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup sample domain client
        domainClient = new Client(
            java.util.UUID.randomUUID(),
            "John",
            "Doe",
            "john.doe@example.com",
            "CUI123"
        );

        // The mapper should convert domain to entity
        entityToSave = new ClientEntity();
        entityToSave.setId(domainClient.getId().toString());
        entityToSave.setFirstName(domainClient.getFirstName());
        entityToSave.setLastName(domainClient.getLastName());
        entityToSave.setEmail(domainClient.getEmail());
        entityToSave.setCui(domainClient.getCui());

        // The saved entity: usually with the same or assigned id
        savedEntity = new ClientEntity();
        savedEntity.setId(domainClient.getId().toString());
        savedEntity.setFirstName(domainClient.getFirstName());
        savedEntity.setLastName(domainClient.getLastName());
        savedEntity.setEmail(domainClient.getEmail());
        savedEntity.setCui(domainClient.getCui());

        // Domain object from saved entity
        domainFromSaved = new Client(
            java.util.UUID.fromString(savedEntity.getId()),
            savedEntity.getFirstName(),
            savedEntity.getLastName(),
            savedEntity.getEmail(),
            savedEntity.getCui()
        );

        // Mock mapper behaviors
        when(clientRepositoryMapper.toEntity(domainClient)).thenReturn(entityToSave);
        when(clientRepository.save(entityToSave)).thenReturn(savedEntity);
        when(clientRepositoryMapper.toDomain(savedEntity)).thenReturn(domainFromSaved);
    }

    @Test
    void createClient_savesAndReturnsDomain() {
        Client result = adapter.createClient(domainClient);

        // Validate the interactions
        verify(clientRepositoryMapper).toEntity(domainClient);
        verify(clientRepository).save(entityToSave);
        verify(clientRepositoryMapper).toDomain(savedEntity);

        // Check result
        assertNotNull(result);
        assertEquals(domainFromSaved, result);
    }

}