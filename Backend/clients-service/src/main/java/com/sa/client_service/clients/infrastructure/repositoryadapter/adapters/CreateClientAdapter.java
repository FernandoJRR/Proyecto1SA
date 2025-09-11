package com.sa.client_service.clients.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.clients.application.outputports.CreateClientOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.repositoryadapter.mappers.ClientRepositoryMapper;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateClientAdapter implements CreateClientOutputPort {

    private final ClientRepository clientRepository;
    private final ClientRepositoryMapper clientRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Client createClient(Client client) {
        ClientEntity clientEntity = clientRepositoryMapper.toEntity(client);
        return clientRepositoryMapper.toDomain(clientRepository.save(clientEntity));
    }

}
