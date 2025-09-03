package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, String> {
    public List<RestaurantEntity> findAllByHotel_Id(String hotelId);
}
