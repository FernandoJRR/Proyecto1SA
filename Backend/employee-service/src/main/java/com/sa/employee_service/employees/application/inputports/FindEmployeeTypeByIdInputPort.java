package com.sa.employee_service.employees.application.inputports;

import java.util.Optional;
import java.util.UUID;

import com.sa.employee_service.employees.domain.EmployeeType;

public interface FindEmployeeTypeByIdInputPort {
    public Optional<EmployeeType> handle(UUID id);
}
