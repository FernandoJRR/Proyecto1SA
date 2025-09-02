package com.sa.employee_service.employees.application.usecases;

import java.util.Optional;
import java.util.UUID;

import com.sa.employee_service.employees.application.inputports.FindEmployeeTypeByIdInputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.domain.EmployeeType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindEmployeeTypeByIdUseCase implements FindEmployeeTypeByIdInputPort {
    private final FindEmployeeTypeByIdOutputPort outputPort;

    public Optional<EmployeeType> handle(UUID id) {
        return outputPort.findEmployeeTypeById(id);
    }
}
