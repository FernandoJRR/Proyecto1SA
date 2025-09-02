package com.sa.employee_service.employees.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByIdInputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByIdOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindEmployeeByIdUseCase implements FindEmployeeByIdInputPort {
    private final FindEmployeeByIdOutputPort findEmployeeByIdOutputPort;

    public Employee handle(String id) throws NotFoundException {
        return findEmployeeByIdOutputPort
            .findEmployeeById(id)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado"));
    }
}
