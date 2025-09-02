package com.sa.employee_service.users.infrastructure.repositoryadapter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    public Boolean existsByUsername(String username);

    public Optional<UserEntity> findByUsername(String usernamme);

}
