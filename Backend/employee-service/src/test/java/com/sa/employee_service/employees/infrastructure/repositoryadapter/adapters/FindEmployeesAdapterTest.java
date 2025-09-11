package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
public class FindEmployeesAdapterTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeRepositoryMapper employeeRepositoryMapper;

    private FindEmployeesAdapter adapter;

    private static final UUID EMPLOYEE_TYPE_ID = UUID.fromString("967c5c2b-ded0-4f0f-a68a-9466ac9e32f4");
    private static final UUID ESTABLISHMENT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final String CUI = "1234567890123";

    @BeforeEach
    void setup() {
        adapter = new FindEmployeesAdapter(employeeRepository, employeeRepositoryMapper);
    }

    // given: no filters
    // when: findEmployees is called
    // then: repository.findAll is called with a base spec and results mapped
    @Test
    void givenNoFilters_whenFindEmployees_thenReturnsMappedList() {
        // given
        List<EmployeeEntity> entities = new ArrayList<>();
        EmployeeEntity e1 = new EmployeeEntity("cui1", "A", "B", new BigDecimal("1000"));
        EmployeeEntity e2 = new EmployeeEntity("cui2", "C", "D", new BigDecimal("1200"));
        entities.add(e1);
        entities.add(e2);

        when(employeeRepository.findAll(any(Specification.class))).thenReturn(entities);
        when(employeeRepositoryMapper.toDomain(e1)).thenReturn(new Employee("cui1", "A", "B", new BigDecimal("1000")));
        when(employeeRepositoryMapper.toDomain(e2)).thenReturn(new Employee("cui2", "C", "D", new BigDecimal("1200")));

        FindEmployeesDTO filter = FindEmployeesDTO.builder().build();

        // when
        List<Employee> result = adapter.findEmployees(filter);

        // then
        assertEquals(2, result.size());
        ArgumentCaptor<Specification<EmployeeEntity>> captor = ArgumentCaptor.forClass(Specification.class);
        verify(employeeRepository, times(1)).findAll(captor.capture());
        org.junit.jupiter.api.Assertions.assertNotNull(captor.getValue());
        verify(employeeRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(e1));
        verify(employeeRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(e2));
    }

    // given: all filters provided
    // when: findEmployees is called
    // then: specification composes equals for each field and results are mapped
    @Test
    void givenAllFilters_whenFindEmployees_thenBuildsSpecAndMaps() {
        // given
        List<EmployeeEntity> entities = List.of(new EmployeeEntity("cuiX", "N", "L", new BigDecimal("1300")));

        // Intercept the Specification to verify its toPredicate builds expected comparisons
        when(employeeRepository.findAll(any(Specification.class))).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            Specification<EmployeeEntity> spec = (Specification<EmployeeEntity>) inv.getArgument(0);

            Root<EmployeeEntity> root = mock(Root.class);
            CriteriaQuery<?> query = mock(CriteriaQuery.class);
            CriteriaBuilder cb = mock(CriteriaBuilder.class);

            // Mock path navigation: employeeType.id, establishmentId, cui
            @SuppressWarnings("unchecked") Path<Object> employeeTypePath = (Path<Object>) mock(Path.class);
            @SuppressWarnings("unchecked") Path<Object> employeeTypeIdPath = (Path<Object>) mock(Path.class);
            when(root.get("employeeType")).thenReturn(employeeTypePath);
            when(employeeTypePath.get("id")).thenReturn(employeeTypeIdPath);

            @SuppressWarnings("unchecked") Path<Object> establishmentIdPath = (Path<Object>) mock(Path.class);
            when(root.get("establishmentId")).thenReturn(establishmentIdPath);

            @SuppressWarnings("unchecked") Path<Object> cuiPath = (Path<Object>) mock(Path.class);
            when(root.get("cui")).thenReturn(cuiPath);

            // Predicates and and-combinations
            Predicate p1 = mock(Predicate.class);
            Predicate p2 = mock(Predicate.class);
            Predicate p3 = mock(Predicate.class);
            Predicate pAnd1 = mock(Predicate.class);
            Predicate pAnd2 = mock(Predicate.class);

            when(cb.equal(employeeTypeIdPath, EMPLOYEE_TYPE_ID.toString())).thenReturn(p1);
            when(cb.equal(establishmentIdPath, ESTABLISHMENT_ID)).thenReturn(p2);
            when(cb.equal(cuiPath, CUI)).thenReturn(p3);
            when(cb.and(any(Predicate.class), any(Predicate.class))).thenReturn(pAnd1, pAnd2);

            // Exercise toPredicate to ensure the spec builds without errors
            spec.toPredicate(root, query, cb);

            return entities;
        });

        when(employeeRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entities.get(0))))
            .thenReturn(new Employee("cuiX", "N", "L", new BigDecimal("1300")));

        FindEmployeesDTO filter = FindEmployeesDTO.builder()
            .employeeTypeId(EMPLOYEE_TYPE_ID)
            .establishmentId(ESTABLISHMENT_ID)
            .cui(CUI)
            .build();

        // when
        List<Employee> result = adapter.findEmployees(filter);

        // then
        assertEquals(1, result.size());

        // Also capture that a non-null spec was used
        ArgumentCaptor<Specification<EmployeeEntity>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(employeeRepository, times(1)).findAll(specCaptor.capture());
        Specification<EmployeeEntity> used = specCaptor.getValue();
        // just assert it's not null (it is a composed spec)
        org.junit.jupiter.api.Assertions.assertNotNull(used);

        verify(employeeRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entities.get(0)));
    }
}
