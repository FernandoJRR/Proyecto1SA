package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CreateEmployeeAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private UserRepository userRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private CreateEmployeeAdapter adapter;

    private static final String USER_ID = "8db25021-8b82-4a60-a52c-d1d00324431";

    @BeforeEach
    void setup() {
        adapter = new CreateEmployeeAdapter(employeeRepository, userRepository, employeeRepositoryMapper);
    }

    // given: user exists and mapper returns entity
    // when: createEmployee is called
    // then: links user-employee both ways, saves and maps back
    @Test
    void givenValidInput_whenCreateEmployee_thenLinksAndSaves() {
        // given
        UserEntity user = new UserEntity(USER_ID, "luis", "pwd");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Employee domainEmp = new Employee("1234567890123", "Michael", "Kane", new BigDecimal("1200"));
        EmployeeEntity entity = new EmployeeEntity("1234567890123", "Michael", "Kane", new BigDecimal("1200"));
        when(employeeRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domainEmp))).thenReturn(entity);
        when(employeeRepository.save(org.mockito.ArgumentMatchers.same(entity))).thenReturn(entity);

        Employee mappedBack = new Employee("1234567890123", "Michael", "Kane", new BigDecimal("1200"));
        when(employeeRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity))).thenReturn(mappedBack);

        // when
        Employee result = adapter.createEmployee(USER_ID, domainEmp);

        // then
        assertAll(
            () -> assertSame(mappedBack, result),
            () -> assertSame(user, entity.getUser()),
            () -> assertSame(entity, user.getEmployee()),
            () -> assertEquals("Michael", result.getFirstName()),
            () -> assertEquals("Kane", result.getLastName())
        );

        verify(userRepository, times(1)).findById(USER_ID);
        verify(employeeRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domainEmp));
        verify(employeeRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(employeeRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }

    // given: user does not exist
    // when: createEmployee is called
    // then: throws NoSuchElementException and does not call mapper/save
    @Test
    void givenMissingUser_whenCreateEmployee_thenThrows() {
        // given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        Employee domainEmp = new Employee("1234567890123", "Ana", "Lopez", new BigDecimal("900"));

        // when - then
        assertThrows(java.util.NoSuchElementException.class, () -> adapter.createEmployee(USER_ID, domainEmp));
        verify(employeeRepositoryMapper, never()).toEntity(org.mockito.ArgumentMatchers.any());
        verify(employeeRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
