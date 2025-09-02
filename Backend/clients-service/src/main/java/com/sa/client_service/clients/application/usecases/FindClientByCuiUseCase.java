package com.sa.client_service.clients.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.inputports.FindClientByCuiInputPort;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindClientByCuiUseCase implements FindClientByCuiInputPort {

    private final FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Override
    public Client handle(String cui) throws NotFoundException {
        return findClientByCuiOutputPort.findByCui(cui)
            .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }

}
