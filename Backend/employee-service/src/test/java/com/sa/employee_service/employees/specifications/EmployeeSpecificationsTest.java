package com.sa.employee_service.employees.specifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.sa.employee_service.employees.application.specifications.EmployeeSpecifications;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeSpecificationsTest {

    private static final String EMPLOYEE_FIRST_NAME = "Luis";
    private static final String EMPLOYEE_LAST_NAME = "Perez";
    private static final String EMPLOYEE_TYPE_ID = "abc-123";

    @Mock
    private Root<EmployeeEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Predicate predicate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * dado: un nombre no nulo
     * cuando: se genera la especificación con hasFirstName
     * entonces: se genera un predicado válido.
     */
    @Test
    public void shouldReturnPredicateForValidFirstName() {
        when(cb.lower(any())).thenReturn(null);
        when(cb.like(any(), anyString())).thenReturn(predicate);

        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasFirstName(EMPLOYEE_FIRST_NAME);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    /**
     * dado: un nombre nulo
     * cuando: se genera la especificación con hasFirstName
     * entonces: se retorna null.
     */
    @Test
    public void shouldReturnNullForNullFirstName() {
        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasFirstName(null);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNull(result);
    }

    /**
     * dado: un apellido no nulo
     * cuando: se genera la especificación con hasLastName
     * entonces: se genera un predicado válido.
     */
    @Test
    public void shouldReturnPredicateForValidLastName() {
        when(cb.lower(any())).thenReturn(null);
        when(cb.like(any(), anyString())).thenReturn(predicate);

        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasLastName(EMPLOYEE_LAST_NAME);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    /**
     * dado: un apellido nulo
     * cuando: se genera la especificación con hasLastName
     * entonces: se retorna null.
     */
    @Test
    public void shouldReturnNullForNullLastName() {
        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasLastName(null);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNull(result);
    }

    /**
     * dado: un ID de tipo de empleado no nulo
     * cuando: se genera la especificación con hasEmployeeTypeId
     * entonces: se genera un predicado válido.
     */
    @Test
    public void shouldReturnPredicateForValidEmployeeTypeId() {
        Path<Object> employeeTypePath = mock(Path.class);
        Path<Object> idPath = mock(Path.class);

        when(root.get("employeeType")).thenReturn(employeeTypePath);
        when(employeeTypePath.get("id")).thenReturn(idPath);
        when(cb.equal(idPath, EMPLOYEE_TYPE_ID)).thenReturn(predicate);

        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasEmployeeTypeId(EMPLOYEE_TYPE_ID);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    /**
     * dado: un ID nulo
     * cuando: se genera la especificación con hasEmployeeTypeId
     * entonces: se retorna null.
     */
    @Test
    public void shouldReturnNullForNullEmployeeTypeId() {
        Specification<EmployeeEntity> spec = EmployeeSpecifications.hasEmployeeTypeId(null);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNull(result);
    }

    /**
     * dado: un valor booleano true
     * cuando: se genera la especificación con isActive
     * entonces: se genera un predicado de isNull para desactivación.
     */
    @Test
    public void shouldReturnPredicateForActiveTrue() {
        when(cb.isNull(any())).thenReturn(predicate);

        Specification<EmployeeEntity> spec = EmployeeSpecifications.isActive(true);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    /**
     * dado: un valor booleano false
     * cuando: se genera la especificación con isActive
     * entonces: se genera un predicado de isNotNull para desactivación.
     */
    @Test
    public void shouldReturnPredicateForActiveFalse() {
        when(cb.isNotNull(any())).thenReturn(predicate);

        Specification<EmployeeEntity> spec = EmployeeSpecifications.isActive(false);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    /**
     * dado: un valor nulo
     * cuando: se genera la especificación con isActive
     * entonces: se retorna null.
     */
    @Test
    public void shouldReturnNullForNullActive() {
        Specification<EmployeeEntity> spec = EmployeeSpecifications.isActive(null);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNull(result);
    }
}
