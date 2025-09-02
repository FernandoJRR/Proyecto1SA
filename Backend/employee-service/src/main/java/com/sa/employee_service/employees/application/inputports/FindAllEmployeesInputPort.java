package com.sa.employee_service.employees.application.inputports;

import java.util.List;

import com.sa.employee_service.employees.domain.Employee;

public interface FindAllEmployeesInputPort {
    public List<Employee> handle();
}
