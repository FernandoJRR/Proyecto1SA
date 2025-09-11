package com.sa.finances_service.payments.application.usecases;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.inputports.FindPaymentsInputPort;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.domain.Payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class FindPaymentsUseCase implements FindPaymentsInputPort {

    private final FindPaymentsOutputPort findPaymentsOutputPort;

    @Override
    public List<Payment> handle(@Valid FindPaymentsDTO filter) {
        return findPaymentsOutputPort.findAll(filter);
    }

}
