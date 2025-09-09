package com.sa.client_service.orders.application.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FindOrdersDTO {
    private LocalDate fromDate;
    private LocalDate toDate;
}
