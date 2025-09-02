package com.sa.client_service.clients.application.inputports;

import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.NotFoundException;

public interface FindClientByCuiInputPort {
    public Client handle(String cui) throws NotFoundException;
}
