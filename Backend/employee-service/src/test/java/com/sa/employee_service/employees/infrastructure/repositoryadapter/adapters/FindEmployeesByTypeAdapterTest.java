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
public class FindEmployeesByTypeAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private FindEmployeesByTypeAdapter adapter;

    private static final String TYPE_ID = "967c5c2b-ded0-4f0f-a68a-9466ac9e32f4";

    @BeforeEach
    void setup() {
        adapter = new FindEmployeesByTypeAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: repository returns employees by type id
    // when: findEmployeesByType is called
    // then: maps to domain and returns list
    @Test
    void givenRepoEmployees_whenFindByType_thenReturnsMappedList() {
        List<EmployeeEntity> entities = List.of(
            new EmployeeEntity("cui1", "A", "B", new BigDecimal("1000")),
            new EmployeeEntity("cui2", "C", "D", new BigDecimal("1200"))
        );
        List<Employee> domain = List.of(
            new Employee("cui1", "A", "B", new BigDecimal("1000")),
            new Employee("cui2", "C", "D", new BigDecimal("1200"))
        );

        when(employeeRepository.findAllByEmployeeType_Id(TYPE_ID)).thenReturn(entities);
        when(employeeRepositoryMapper.toDomain(entities)).thenReturn(domain);

        List<Employee> result = adapter.findEmployeesByType(TYPE_ID);

        assertEquals(domain, result);
        verify(employeeRepository, times(1)).findAllByEmployeeType_Id(TYPE_ID);
        verify(employeeRepositoryMapper, times(1)).toDomain(entities);
    }
}

