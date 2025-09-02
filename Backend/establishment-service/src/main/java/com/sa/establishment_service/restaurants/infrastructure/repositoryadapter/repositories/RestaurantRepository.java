package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, String> {

}
