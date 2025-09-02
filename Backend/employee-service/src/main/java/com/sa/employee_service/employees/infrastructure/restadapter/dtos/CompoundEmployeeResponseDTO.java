package com.sa.employee_service.employees.infrastructure.restadapter.dtos;

import java.util.List;

import com.sa.sharedEmployeeService.dto.EmployeeResponseDTO;

import lombok.Value;

@Value
public class CompoundEmployeeResponseDTO {
    EmployeeResponseDTO employeeResponseDTO;
    String username;
    List<EmployeeHistoryResponseDTO> employeeHistories;
}
