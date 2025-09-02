package com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    public Optional<PermissionEntity> findByName(String name);

    public boolean existsByName(String name);
}
