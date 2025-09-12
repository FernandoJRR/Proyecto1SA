package com.sa.employee_service.employees.application.outputports;

import com.sa.employee_service.employees.domain.Employee;

public interface UpdateEmployeeOutputPort {
    public Employee updateEmployee(Employee employee);
}
