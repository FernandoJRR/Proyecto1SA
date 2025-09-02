package com.sa.employee_service.employees.application.inputports;

import java.util.Optional;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

public interface FindEmployeeByIdInputPort {
    public Employee handle(String id) throws NotFoundException;
}
