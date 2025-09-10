package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.FindEmployeeByIdOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindEmployeeByIdAdapter implements FindEmployeeByIdOutputPort {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(String id) {
        return employeeRepository.findById(id).map(employeeRepositoryMapper::toDomain);
    }


}
