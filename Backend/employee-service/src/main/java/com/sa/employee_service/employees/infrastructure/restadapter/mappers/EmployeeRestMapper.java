package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.dtos.UpdateEmployeeDTO;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.CreateEmployeeRequest;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.UpdateEmployeeRequest;
import com.sa.employee_service.users.infrastructure.restadapter.mappers.UserRestMapper;

@Mapper(componentModel = "spring", uses = UserRestMapper.class)
public interface EmployeeRestMapper {

    @Mapping(source = "createUserRequest", target = "createUserDTO")
    public CreateEmployeeDTO toDTO(CreateEmployeeRequest createEmployeeRequest);

    public UpdateEmployeeDTO toDTO(UpdateEmployeeRequest updateEmployeeRequest);
}
