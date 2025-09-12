package com.sa.employee_service.employees.application.usecases;

import org.springframework.transaction.annotation.Transactional;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.dtos.UpdateEmployeeDTO;
import com.sa.employee_service.employees.application.inputports.UpdateEmployeeInputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.UpdateEmployeeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateEmployeeUseCase implements UpdateEmployeeInputPort {
    private final FindEmployeeByIdOutputPort findEmployeeByIdOutputPort;
    private final UpdateEmployeeOutputPort updateEmployeeOutputPort;

    @Override
    @Transactional
    public Employee handle(String employeeId, @Valid UpdateEmployeeDTO updateEmployeeDTO) throws NotFoundException {
        Employee existing = findEmployeeByIdOutputPort
            .findEmployeeById(employeeId)
            .orElseThrow(() -> new NotFoundException("El empleado buscado no existe"));

        existing.setFirstName(updateEmployeeDTO.getFirstName());
        existing.setLastName(updateEmployeeDTO.getLastName());
        existing.setSalary(updateEmployeeDTO.getSalary());

        return updateEmployeeOutputPort.updateEmployee(existing);
    }
}
