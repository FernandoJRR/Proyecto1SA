package com.sa.client_service.orders.infrastructure.repositoryadapter.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {

    @Query("""
        SELECT oi.dishId
        FROM OrderItemEntity oi
        WHERE oi.order.restaurantId = :restaurantId
        GROUP BY oi.dishId
        ORDER BY COUNT(oi) DESC
    """)
    List<UUID> findTopDishesByRestaurant(
        @Param("restaurantId") UUID restaurantId);

    @Query("""
       select r
       from OrderEntity r
       group by r.restaurantId
       order by count(r) desc
       """)
    List<OrderEntity> findTopRestaurants(Pageable pageable);

    List<OrderEntity> findByRestaurantId(UUID restaurantId);
}
