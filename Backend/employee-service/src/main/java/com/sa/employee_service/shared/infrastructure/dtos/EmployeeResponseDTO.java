package com.sa.employee_service.shared.infrastructure.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;

@Value
public class EmployeeResponseDTO {

    String id;

    String cui;

    String firstName;

    String lastName;

    BigDecimal salary;

    LocalDate desactivatedAt;

    EmployeeTypeResponseDTO employeeType;

    String establishmentId;
    String establishmentType;
}
