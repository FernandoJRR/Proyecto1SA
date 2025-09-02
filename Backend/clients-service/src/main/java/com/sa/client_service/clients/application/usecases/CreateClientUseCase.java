package com.sa.client_service.clients.application.usecases;

import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.dtos.CreateClientDTO;
import com.sa.client_service.clients.application.inputports.CreateClientInputPort;
import com.sa.client_service.clients.application.outputports.CreateClientOutputPort;
import com.sa.client_service.clients.application.outputports.FindClientByEmailOrCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.DuplicatedEntryException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreateClientUseCase implements CreateClientInputPort {

    private final CreateClientOutputPort createClientOutputPort;
    private final FindClientByEmailOrCuiOutputPort findClientByEmailOrCuiOutputPort;

    @Override
    public Client handle(@Valid CreateClientDTO createClientDTO) throws DuplicatedEntryException {

        Optional<Client> foundClient = findClientByEmailOrCuiOutputPort.findByEmailOrCui(createClientDTO.getEmail(), createClientDTO.getCui());
        if (foundClient.isPresent()) {
            throw new DuplicatedEntryException("Ya existe un cliente con el mismo email o CUI.");
        }

        Client registeredClient = Client.register(
            createClientDTO.getFirstName(),
            createClientDTO.getLastName(),
            createClientDTO.getEmail(),
            createClientDTO.getCui()
        );

        return createClientOutputPort.createClient(registeredClient);
    }

}
