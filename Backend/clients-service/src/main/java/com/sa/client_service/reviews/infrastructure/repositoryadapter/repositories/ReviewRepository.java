package com.sa.client_service.reviews.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, String>, JpaSpecificationExecutor<ReviewEntity> {
}
