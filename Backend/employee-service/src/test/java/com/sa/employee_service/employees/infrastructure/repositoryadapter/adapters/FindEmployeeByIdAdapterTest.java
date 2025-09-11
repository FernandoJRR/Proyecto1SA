package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

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
public class FindEmployeeByIdAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private FindEmployeeByIdAdapter adapter;

    private static final String EMPLOYEE_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setup() {
        adapter = new FindEmployeeByIdAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: employee exists in repository
    // when: findEmployeeById is called
    // then: maps to domain and returns present Optional
    @Test
    void givenExistingEmployee_whenFindById_thenReturnsDomain() {
        EmployeeEntity entity = new EmployeeEntity("cui1", "A", "B", new BigDecimal("1000"));
        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(entity));
        Employee domain = new Employee("cui1", "A", "B", new BigDecimal("1000"));
        when(employeeRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        Optional<Employee> result = adapter.findEmployeeById(EMPLOYEE_ID);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
        verify(employeeRepositoryMapper, times(1)).toDomain(same(entity));
    }

    // given: employee not found
    // when: findEmployeeById is called
    // then: returns empty and does not map
    @Test
    void givenMissingEmployee_whenFindById_thenReturnsEmpty() {
        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

        Optional<Employee> result = adapter.findEmployeeById(EMPLOYEE_ID);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
        verify(employeeRepositoryMapper, never()).toDomain(any(EmployeeEntity.class));
    }
}

