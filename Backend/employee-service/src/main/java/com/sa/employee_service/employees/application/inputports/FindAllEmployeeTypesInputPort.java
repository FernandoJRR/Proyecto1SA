package com.sa.employee_service.employees.application.inputports;

import java.util.List;

import com.sa.employee_service.employees.domain.EmployeeType;

public interface FindAllEmployeeTypesInputPort {
    public List<EmployeeType> handle();
}
