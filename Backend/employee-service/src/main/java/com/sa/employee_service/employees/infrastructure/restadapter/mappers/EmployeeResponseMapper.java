package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.shared.infrastructure.dtos.EmployeeResponseDTO;

@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {
    public EmployeeResponseDTO toResponse(Employee employee);
    public List<EmployeeResponseDTO> toResponse(List<Employee> employees);
}
