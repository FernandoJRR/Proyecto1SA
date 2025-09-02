package com.sa.employee_service.employees.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByUsernameInputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByUsernameOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindEmployeeByUsernameUseCase implements FindEmployeeByUsernameInputPort {
    private final FindEmployeeByUsernameOutputPort findEmployeeByUsernameOutputPort;

    public Employee handle(String username) throws NotFoundException {
        return findEmployeeByUsernameOutputPort
            .findEmployeeByUsername(username)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado"));
    }
}
