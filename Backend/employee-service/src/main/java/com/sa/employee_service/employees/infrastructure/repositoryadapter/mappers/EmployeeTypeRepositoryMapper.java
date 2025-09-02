package com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers;

import java.util.Optional;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeTypeRepositoryMapper {
    public EmployeeTypeEntity toEntity(EmployeeType employeeType);
    public EmployeeType toDomain(EmployeeTypeEntity employeeTypeEntity);
}
