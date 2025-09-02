package com.sa.employee_service.employees.application.outputports;

import java.util.Optional;

import com.sa.employee_service.employees.domain.Employee;

public interface FindEmployeeByUsernameOutputPort {
    public Optional<Employee> findEmployeeByUsername(String username);
}
