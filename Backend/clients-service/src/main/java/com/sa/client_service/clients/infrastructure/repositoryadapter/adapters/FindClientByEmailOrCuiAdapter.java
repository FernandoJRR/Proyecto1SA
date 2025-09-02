package com.sa.client_service.clients.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.client_service.clients.application.outputports.FindClientByEmailOrCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.repositoryadapter.mappers.ClientRepositoryMapper;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindClientByEmailOrCuiAdapter implements FindClientByEmailOrCuiOutputPort {

    private final ClientRepository clientRepository;
    private final ClientRepositoryMapper clientRepositoryMapper;

    @Override
    public Optional<Client> findByEmailOrCui(String email, String cui) {
         return clientRepository.findFirstByEmailOrCui(email, cui)
            .map(clientRepositoryMapper::toDomain);
    }

}
