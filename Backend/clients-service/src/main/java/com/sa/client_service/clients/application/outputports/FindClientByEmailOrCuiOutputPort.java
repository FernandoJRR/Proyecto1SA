package com.sa.client_service.clients.application.outputports;

import java.util.Optional;

import com.sa.client_service.clients.domain.Client;

public interface FindClientByEmailOrCuiOutputPort {
    public Optional<Client> findByEmailOrCui(String email, String cui);
}
