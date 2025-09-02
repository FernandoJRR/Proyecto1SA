package com.sa.employee_service.employees.application.inputports;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

public interface FindEmployeeByUsernameInputPort {
    public Employee handle(String username) throws NotFoundException;
}
