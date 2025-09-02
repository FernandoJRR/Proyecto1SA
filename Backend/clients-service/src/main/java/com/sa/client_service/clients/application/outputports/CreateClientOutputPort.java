package com.sa.client_service.clients.application.outputports;

import com.sa.client_service.clients.domain.Client;

public interface CreateClientOutputPort {
    public Client createClient(Client client);
}
