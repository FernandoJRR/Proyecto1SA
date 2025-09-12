package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateEmployeeAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private UpdateEmployeeAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new UpdateEmployeeAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: valid domain employee and mapper
    // when: updateEmployee is called
    // then: maps to entity, saves, maps back to domain
    @Test
    void givenValidEmployee_whenUpdate_thenSavesAndMapsBack() {
        Employee domain = new Employee("1234567890123", "Ana", "Lopez", new BigDecimal("1500.00"));
        EmployeeEntity entity = new EmployeeEntity("1234567890123", "Ana", "Lopez", new BigDecimal("1500.00"));
        when(employeeRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domain))).thenReturn(entity);
        when(employeeRepository.save(org.mockito.ArgumentMatchers.same(entity))).thenReturn(entity);

        Employee mapped = new Employee("1234567890123", "Ana", "Lopez", new BigDecimal("1500.00"));
        when(employeeRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity))).thenReturn(mapped);

        Employee result = adapter.updateEmployee(domain);

        assertSame(mapped, result);
        verify(employeeRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domain));
        verify(employeeRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(employeeRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }
}

