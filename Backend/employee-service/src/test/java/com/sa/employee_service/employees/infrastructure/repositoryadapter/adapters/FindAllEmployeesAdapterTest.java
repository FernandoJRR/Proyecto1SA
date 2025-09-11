package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

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
public class FindAllEmployeesAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private FindAllEmployeesAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindAllEmployeesAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: repository has employees
    // when: findAllEmployees is called
    // then: maps list and returns it
    @Test
    void givenEmployeesInRepo_whenFindAll_thenReturnsMappedList() {
        List<EmployeeEntity> entities = List.of(
            new EmployeeEntity("cui1", "A", "B", new BigDecimal("1000")),
            new EmployeeEntity("cui2", "C", "D", new BigDecimal("1200"))
        );
        List<Employee> domain = List.of(
            new Employee("cui1", "A", "B", new BigDecimal("1000")),
            new Employee("cui2", "C", "D", new BigDecimal("1200"))
        );

        when(employeeRepository.findAll()).thenReturn(entities);
        when(employeeRepositoryMapper.toDomain(entities)).thenReturn(domain);

        List<Employee> result = adapter.findAllEmployees();

        assertEquals(domain, result);
        verify(employeeRepository, times(1)).findAll();
        verify(employeeRepositoryMapper, times(1)).toDomain(entities);
    }
}

