package com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PaymentType;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, String> {
}
