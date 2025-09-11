package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeTypeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeTypeRepository;

@ExtendWith(MockitoExtension.class)
public class FindEmployeeTypeByIdAdapterTest {

    @Mock private EmployeeTypeRepository employeeTypeRepository;
    @Mock private EmployeeTypeRepositoryMapper employeeTypeRepositoryMapper;

    private FindEmployeeTypeByIdAdapter adapter;

    private static final UUID TYPE_ID = UUID.fromString("967c5c2b-ded0-4f0f-a68a-9466ac9e32f4");

    @BeforeEach
    void setup() {
        adapter = new FindEmployeeTypeByIdAdapter(employeeTypeRepository, employeeTypeRepositoryMapper);
    }

    // given: employee type exists in repository
    // when: findEmployeeTypeById is called
    // then: maps entity to domain and returns present Optional
    @Test
    void givenExistingType_whenFindById_thenReturnsDomain() {
        // given
        String idStr = TYPE_ID.toString();
        EmployeeTypeEntity entity = new EmployeeTypeEntity(idStr, "Waiter");
        when(employeeTypeRepository.findById(idStr)).thenReturn(Optional.of(entity));
        EmployeeType domain = new EmployeeType(TYPE_ID, "Waiter");
        when(employeeTypeRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        // when
        Optional<EmployeeType> result = adapter.findEmployeeTypeById(TYPE_ID);

        // then
        assertTrue(result.isPresent());
        assertEquals("Waiter", result.get().getName());
        verify(employeeTypeRepository, times(1)).findById(idStr);
        verify(employeeTypeRepositoryMapper, times(1)).toDomain(same(entity));
    }

    // given: employee type does not exist
    // when: findEmployeeTypeById is called
    // then: returns empty Optional and does not map
    @Test
    void givenMissingType_whenFindById_thenReturnsEmpty() {
        // given
        String idStr = TYPE_ID.toString();
        when(employeeTypeRepository.findById(idStr)).thenReturn(Optional.empty());

        // when
        Optional<EmployeeType> result = adapter.findEmployeeTypeById(TYPE_ID);

        // then
        assertFalse(result.isPresent());
        verify(employeeTypeRepository, times(1)).findById(idStr);
        verify(employeeTypeRepositoryMapper, never()).toDomain(org.mockito.ArgumentMatchers.any(com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity.class));
    }
}
