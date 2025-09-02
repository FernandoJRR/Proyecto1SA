package com.sa.employee_service.employees.application.outputports;

import java.util.Optional;
import java.util.UUID;

import com.sa.employee_service.employees.domain.EmployeeType;

public interface FindEmployeeTypeByIdOutputPort {
    public Optional<EmployeeType> findEmployeeTypeById(UUID id);
}
