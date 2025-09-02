package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.CreateEmployeeRequest;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.EmployeeRequestDTO;
import com.sa.employee_service.shared.infrastructure.dtos.EmployeeResponseDTO;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    public EmployeeEntity fromCreateEmployeeRequestDtoToEmployee(CreateEmployeeRequest dto);

    public EmployeeEntity fromEmployeeRequestDtoToEmployee(EmployeeRequestDTO dto);

    public EmployeeResponseDTO fromEmployeeToResponse(EmployeeEntity employee);

    public List<EmployeeResponseDTO> fromEmployeesToResponse(List<EmployeeEntity> employees);

}
