package com.sa.establishment_service.shared.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.establishment_service.shared.infrastructure.restadapter.dtos.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    public PaymentResponse toResponse(PaymentDTO dto);
    public List<PaymentResponse> toResponse(List<PaymentDTO> dto);
}
