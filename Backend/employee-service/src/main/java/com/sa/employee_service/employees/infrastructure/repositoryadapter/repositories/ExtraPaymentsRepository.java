package com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.ExtraPayment;

public interface ExtraPaymentsRepository extends JpaRepository<ExtraPayment, String>{

}
