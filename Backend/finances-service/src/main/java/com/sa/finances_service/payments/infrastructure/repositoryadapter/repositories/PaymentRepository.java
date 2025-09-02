package com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

}
