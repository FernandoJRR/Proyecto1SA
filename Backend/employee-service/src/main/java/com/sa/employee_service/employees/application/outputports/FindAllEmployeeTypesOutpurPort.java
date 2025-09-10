package com.sa.employee_service.employees.application.outputports;

import java.util.List;

import com.sa.employee_service.employees.domain.EmployeeType;

public interface FindAllEmployeeTypesOutpurPort {
    public List<EmployeeType> findAll();
}
