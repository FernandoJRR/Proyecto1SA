package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.CreateEmployeeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateEmployeeAdapter implements CreateEmployeeOutputPort {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Employee createEmployee(String userId, Employee employee) {
        UserEntity foundUser = userRepository.findById(userId).get();
        EmployeeEntity entity = employeeRepositoryMapper.toEntity(employee);
        entity.setUser(foundUser);
        foundUser.setEmployee(entity);
        EmployeeEntity saved = employeeRepository.save(entity);
        return employeeRepositoryMapper.toDomain(saved);
    }
}
