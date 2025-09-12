package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.UpdateEmployeeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UpdateEmployeeAdapter implements UpdateEmployeeOutputPort {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Employee updateEmployee(Employee employee) {
        EmployeeEntity entity = employeeRepositoryMapper.toEntity(employee);
        EmployeeEntity saved = employeeRepository.save(entity);
        return employeeRepositoryMapper.toDomain(saved);
    }
}
