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
public class FindEmployeeByUsernameAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private FindEmployeeByUsernameAdapter adapter;

    private static final String USERNAME = "luis";

    @BeforeEach
    void setup() {
        adapter = new FindEmployeeByUsernameAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: employee exists by username
    // when: findEmployeeByUsername is called
    // then: repository returns entity, mapper maps, present Optional
    @Test
    void givenExistingEmployee_whenFindByUsername_thenReturnsDomain() {
        EmployeeEntity entity = new EmployeeEntity("cui1", "A", "B", new BigDecimal("1000"));
        when(employeeRepository.findByUser_Username(USERNAME)).thenReturn(Optional.of(entity));
        Employee domain = new Employee("cui1", "A", "B", new BigDecimal("1000"));
        when(employeeRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        Optional<Employee> result = adapter.findEmployeeByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getFirstName());
        verify(employeeRepository, times(1)).findByUser_Username(USERNAME);
        verify(employeeRepositoryMapper, times(1)).toDomain(same(entity));
    }

    // given: employee not found by username
    // when: findEmployeeByUsername is called
    // then: returns empty and does not call mapper
    @Test
    void givenMissingEmployee_whenFindByUsername_thenReturnsEmpty() {
        when(employeeRepository.findByUser_Username(USERNAME)).thenReturn(Optional.empty());

        Optional<Employee> result = adapter.findEmployeeByUsername(USERNAME);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findByUser_Username(USERNAME);
        verify(employeeRepositoryMapper, never()).toDomain(any(EmployeeEntity.class));
    }
}

