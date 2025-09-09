package com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String>, JpaSpecificationExecutor<PaymentEntity> {

    @Query("SELECT p.clientId FROM PaymentEntity p " +
           "GROUP BY p.clientId " +
           "ORDER BY COUNT(p) DESC")
    List<String> findTopClientsByPaymentCount(PageRequest pageable);
}
