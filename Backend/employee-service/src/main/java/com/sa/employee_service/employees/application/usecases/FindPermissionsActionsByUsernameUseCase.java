package com.sa.employee_service.employees.application.usecases;

import com.sa.employee_service.employees.application.outputports.FindEmployeeByUsernameOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindPermissionsActionsByUsernameInputPort;

@UseCase
@RequiredArgsConstructor
public class FindPermissionsActionsByUsernameUseCase implements FindPermissionsActionsByUsernameInputPort {
    private final FindEmployeeByUsernameOutputPort findEmployeeByUsernameOutputPort;

    @Override
    public List<String> handle(String username) throws NotFoundException {
        Employee employee = findEmployeeByUsernameOutputPort.findEmployeeByUsername(username)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado"));

        return employee.getEmployeeType().getPermissions().stream()
                .map(permission -> permission.getAction()).toList();
    }
}
