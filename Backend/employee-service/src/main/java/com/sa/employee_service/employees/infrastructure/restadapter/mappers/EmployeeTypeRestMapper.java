package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.shared.infrastructure.dtos.EmployeeTypeResponseDTO;

@Mapper(componentModel = "spring")
public interface EmployeeTypeRestMapper {
    public EmployeeTypeResponseDTO toResponse(EmployeeType employeeType);
    public List<EmployeeTypeResponseDTO> toResponse(List<EmployeeType> employeeType);
}
