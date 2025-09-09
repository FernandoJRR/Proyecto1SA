package com.sa.client_service.clients.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentRestMapper {
    public PaymentResponse toResponse(PaymentDTO dto);
    public List<PaymentResponse> toResponse(List<PaymentDTO> dto);
}
