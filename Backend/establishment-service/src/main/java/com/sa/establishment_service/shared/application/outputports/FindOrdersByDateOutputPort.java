package com.sa.establishment_service.shared.application.outputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.establishment_service.shared.application.dtos.OrderDTO;

public interface FindOrdersByDateOutputPort {
    public List<OrderDTO> findOrdersByDate(LocalDate fromDate, LocalDate toDate);
}
