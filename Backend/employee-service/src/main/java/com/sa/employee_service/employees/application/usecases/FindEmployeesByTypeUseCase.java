package com.sa.employee_service.employees.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindEmployeesByTypeInputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeesByTypeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindEmployeesByTypeUseCase implements FindEmployeesByTypeInputPort {
    private final FindEmployeesByTypeOutputPort findEmployeesByTypeOutputPort;
    private final FindEmployeeTypeByIdOutputPort findEmployeeTypeByIdOutputPort;

    public List<Employee> handle(String employeeTypeId, UUID establishmentId) throws NotFoundException {
        findEmployeeTypeByIdOutputPort.findEmployeeTypeById(UUID.fromString(employeeTypeId))
            .orElseThrow(() -> new NotFoundException("El tipo de empleado no existe"));

        return findEmployeesByTypeOutputPort.findEmployeesByType(employeeTypeId);
    }

}
