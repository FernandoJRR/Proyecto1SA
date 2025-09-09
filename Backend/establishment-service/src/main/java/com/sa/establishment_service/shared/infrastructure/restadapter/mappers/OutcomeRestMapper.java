package com.sa.establishment_service.shared.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.establishment_service.shared.application.dtos.OutcomeDTO;
import com.sa.establishment_service.shared.infrastructure.restadapter.dtos.OutcomeResponse;

@Mapper(componentModel = "spring")
public interface OutcomeRestMapper {
    public OutcomeResponse toResponse(OutcomeDTO dto);
}
