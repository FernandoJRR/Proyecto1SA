package com.sa.employee_service.employees.application.outputports;

import java.util.List;
import java.util.UUID;

import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.domain.Employee;

public interface FindEmployeesOutputPort {
    public List<Employee> findEmployees(FindEmployeesDTO dto);
}
