package com.sa.employee_service.employees.application.outputports;

import java.util.List;

import com.sa.employee_service.employees.domain.Employee;

public interface FindAllEmployeesOutputPort {
    public List<Employee> findAllEmployees();
}
