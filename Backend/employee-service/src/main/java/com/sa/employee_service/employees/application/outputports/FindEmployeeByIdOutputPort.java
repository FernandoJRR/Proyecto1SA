package com.sa.employee_service.employees.application.outputports;

import java.util.Optional;

import com.sa.employee_service.employees.domain.Employee;

public interface FindEmployeeByIdOutputPort {
    public Optional<Employee> findEmployeeById(String id);
}
