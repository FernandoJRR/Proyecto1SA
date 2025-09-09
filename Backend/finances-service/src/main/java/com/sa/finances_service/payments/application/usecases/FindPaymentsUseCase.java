package com.sa.finances_service.payments.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.inputports.FindPaymentsInputPort;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.domain.Payment;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindPaymentsUseCase implements FindPaymentsInputPort {

    private final FindPaymentsOutputPort findPaymentsOutputPort;

    @Override
    public List<Payment> handle(FindPaymentsDTO filter) {
        return findPaymentsOutputPort.findAll(filter);
    }

}
