package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;

public interface HotelRepository extends JpaRepository<HotelEntity, String> {

}
