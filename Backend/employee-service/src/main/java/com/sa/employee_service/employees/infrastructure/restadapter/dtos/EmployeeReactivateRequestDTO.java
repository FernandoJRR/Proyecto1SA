package com.sa.employee_service.employees.infrastructure.restadapter.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeReactivateRequestDTO {
    private LocalDate reactivationDate;
}
