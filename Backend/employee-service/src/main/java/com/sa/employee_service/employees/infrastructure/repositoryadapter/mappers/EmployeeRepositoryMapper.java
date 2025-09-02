package com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeRepositoryMapper {
    public EmployeeEntity toEntity(Employee employee);
    public Employee toDomain(EmployeeEntity employeeEntity);
    public List<Employee> toDomain(List<EmployeeEntity> employeeEntity);
}
