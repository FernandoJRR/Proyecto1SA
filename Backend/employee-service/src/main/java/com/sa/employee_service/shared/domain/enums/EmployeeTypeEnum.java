package com.sa.employee_service.shared.domain.enums;

import com.sa.employee_service.employees.domain.EmployeeType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que define los tipos de empleados predefinidos en el sistema.
 */
@Getter
@AllArgsConstructor
public enum EmployeeTypeEnum {

    DEFAULT(new EmployeeType(null,"Sin Asignar")),
    ADMIN(new EmployeeType(null,"Admin")),
    STAFF_HOTEL(new EmployeeType(null,"Staff Hotel")),
    STAFF_RESTAURANT(new EmployeeType(null,"Staff Restaurante")),
    CONTADOR(new EmployeeType(null,"Contador"));

    private final EmployeeType employeeType;
}
