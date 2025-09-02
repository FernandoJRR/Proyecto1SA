package com.sa.client_service.clients.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientRepositoryMapper {
    public Client toDomain(ClientEntity clientEntity);
    public ClientEntity toEntity(Client client);
}
