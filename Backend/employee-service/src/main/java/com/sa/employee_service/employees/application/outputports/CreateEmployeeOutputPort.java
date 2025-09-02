package com.sa.employee_service.employees.application.outputports;

import com.sa.employee_service.employees.domain.Employee;

public interface CreateEmployeeOutputPort {
    Employee createEmployee(String userId, Employee employee);
}
