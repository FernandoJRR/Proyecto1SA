package com.sa.employee_service.employees.application.inputports;

import com.sa.employee_service.employees.application.dtos.UpdateEmployeeDTO;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface UpdateEmployeeInputPort {
    Employee handle(String employeeId, @Valid UpdateEmployeeDTO updateEmployeeDTO) throws NotFoundException;
}

