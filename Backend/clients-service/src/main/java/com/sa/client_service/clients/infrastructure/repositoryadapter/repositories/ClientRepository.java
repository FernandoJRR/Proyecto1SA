package com.sa.client_service.clients.infrastructure.repositoryadapter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {
    Optional<ClientEntity> findFirstByEmailOrCui(String email, String cui);
    Optional<ClientEntity> findFirstByCui(String cui);
}
