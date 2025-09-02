package com.sa.client_service.clients.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.client_service.clients.application.dtos.CreateClientDTO;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.ClientResponse;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.CreateClientRequest;

@Mapper(componentModel = "spring")
public interface ClientRestMapper {
    public CreateClientDTO toDTO(CreateClientRequest clientRequest);
    public ClientResponse toResponse(Client client);
}
