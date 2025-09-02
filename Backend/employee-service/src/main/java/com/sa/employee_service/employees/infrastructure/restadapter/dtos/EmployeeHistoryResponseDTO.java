package com.sa.employee_service.employees.infrastructure.restadapter.dtos;

import com.sa.employee_service.shared.infrastructure.dtos.EmployeeResponseDTO;

import lombok.Value;

@Value
public class EmployeeHistoryResponseDTO {

    EmployeeResponseDTO employee;

    HistoryTypeResponseDTO historyType;

    String commentary;

    String historyDate;
}
