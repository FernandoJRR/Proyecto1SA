package com.sa.finances_service.payments.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.payments.application.inputports.FindPaymentByIdInputPort;
import com.sa.finances_service.payments.application.outputports.FindPaymentByIdOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindPaymentByIdUseCase implements FindPaymentByIdInputPort {

    private final FindPaymentByIdOutputPort findPaymentByIdOutputPort;

    @Override
    public Payment handle(String id) throws NotFoundException {
        return findPaymentByIdOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("El pago buscado no existe"));
    }

}
