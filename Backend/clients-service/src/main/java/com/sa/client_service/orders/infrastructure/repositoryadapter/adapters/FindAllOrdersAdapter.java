package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.orders.application.dtos.FindOrdersDTO;
import com.sa.client_service.orders.application.outputports.FindAllOrdersOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllOrdersAdapter implements FindAllOrdersOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    public List<Order> findAll(FindOrdersDTO filter) {
        Specification<OrderEntity> spec = buildSpec(filter.getFromDate(), filter.getToDate());
        List<OrderEntity> result = orderRepository.findAll(spec);
        return orderRepositoryMapper.toDomain(result);
    }

    private Specification<OrderEntity> buildSpec(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) ->
            (fromDate != null && toDate != null)
                ? cb.between(root.get("orderedAt"), fromDate, toDate)
                : (fromDate != null)
                    ? cb.greaterThanOrEqualTo(root.get("orderedAt"), fromDate)
                    : (toDate != null)
                        ? cb.lessThanOrEqualTo(root.get("orderedAt"), toDate)
                        : cb.conjunction();
    }
}
