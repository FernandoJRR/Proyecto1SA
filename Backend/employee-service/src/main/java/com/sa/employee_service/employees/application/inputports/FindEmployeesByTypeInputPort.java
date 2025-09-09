package com.sa.employee_service.employees.application.inputports;

import java.util.List;
import java.util.UUID;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

public interface FindEmployeesByTypeInputPort {
    public List<Employee> handle(String employeeTypeId, UUID establishmentId) throws NotFoundException;
}
