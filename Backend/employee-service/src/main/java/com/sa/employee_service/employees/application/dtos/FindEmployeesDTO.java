package com.sa.employee_service.employees.application.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindEmployeesDTO {
    private UUID employeeTypeId;
    private UUID establishmentId;
    private String cui;
}
