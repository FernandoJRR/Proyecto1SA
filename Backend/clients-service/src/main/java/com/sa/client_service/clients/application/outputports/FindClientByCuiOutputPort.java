package com.sa.client_service.clients.application.outputports;

import java.util.Optional;

import com.sa.client_service.clients.domain.Client;

public interface FindClientByCuiOutputPort {
    public Optional<Client> findByCui(String cui);
}
