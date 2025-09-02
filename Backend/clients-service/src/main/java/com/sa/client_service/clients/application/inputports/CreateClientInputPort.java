package com.sa.client_service.clients.application.inputports;

import com.sa.client_service.clients.application.dtos.CreateClientDTO;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.DuplicatedEntryException;

import jakarta.validation.Valid;

public interface CreateClientInputPort {
    public Client handle(@Valid CreateClientDTO createClientDTO) throws DuplicatedEntryException;
}
