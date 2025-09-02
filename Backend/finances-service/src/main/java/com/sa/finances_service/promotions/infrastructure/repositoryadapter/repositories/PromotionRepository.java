package com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, String> {

}
