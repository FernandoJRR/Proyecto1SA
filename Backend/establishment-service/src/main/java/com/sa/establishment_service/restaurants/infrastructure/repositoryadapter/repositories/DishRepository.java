package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, String> {
    @Query("""
        select d
        from dish d
        where d.restaurant.id = :restaurantId and d.id in :ids
      """)
    List<DishEntity> findExistant(@Param("restaurantId") String restaurantId,
       @Param("ids") List<String> ids);
}
