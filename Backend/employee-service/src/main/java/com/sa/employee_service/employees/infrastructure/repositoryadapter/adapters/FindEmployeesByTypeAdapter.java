package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.FindEmployeesByTypeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindEmployeesByTypeAdapter implements FindEmployeesByTypeOutputPort{
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findEmployeesByType(String employeeTypeId) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAllByEmployeeType_Id(employeeTypeId);
        return employeeRepositoryMapper.toDomain(employeeEntities);
    }
}
