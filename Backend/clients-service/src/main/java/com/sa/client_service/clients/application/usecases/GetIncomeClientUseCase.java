package com.sa.client_service.clients.application.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.client_service.clients.application.inputports.GetIncomeByClientInputPort;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.application.outputports.FindPaymentsByClientOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.InvalidParameterException;

import com.sa.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetIncomeClientUseCase implements GetIncomeByClientInputPort {

    private final FindClientByCuiOutputPort findClientByCuiOutputPort;
    private final FindPaymentsByClientOutputPort findPaymentsByClientOutputPort;

    @Override
    public List<PaymentDTO> handle(String clientCui, String establishmentId, LocalDate fromDate, LocalDate toDate) throws InvalidParameterException, NotFoundException {
            Client foundClient = findClientByCuiOutputPort.findByCui(clientCui)
                .orElseThrow(() -> new NotFoundException("El cliente ingresado no existe"));

            return findPaymentsByClientOutputPort.findByClient(foundClient.getId().toString(),
                        establishmentId, fromDate, toDate)
                .stream()
                .peek(p -> p.setDescription("Consumo"))
                .toList();
    }

}
