package com.sa.employee_service.employees.application.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;

public class EmployeeSpecifications {

    public static Specification<EmployeeEntity> hasFirstName(String firstName) {
        return (root, query, cb) ->
                firstName == null ? null : cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> hasEmployeeTypeId(String employeeTypeId) {
        return (root, query, cb) ->
                employeeTypeId == null ? null : cb.equal(root.get("employeeType").get("id"), employeeTypeId);
    }

    public static Specification<EmployeeEntity> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return null;
            if (active)
                return cb.isNull(root.get("desactivatedAt"));
            else
                return cb.isNotNull(root.get("desactivatedAt"));
        };
    }
}
