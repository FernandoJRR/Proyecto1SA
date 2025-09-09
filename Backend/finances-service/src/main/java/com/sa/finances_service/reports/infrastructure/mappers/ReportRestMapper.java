package com.sa.finances_service.reports.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.sa.finances_service.reports.application.dtos.IncomeOutcomeDTO;
import com.sa.finances_service.reports.infrastructure.dtos.IncomeOutcomeResponse;

@Mapper(componentModel = "spring")
public interface ReportRestMapper {
    public IncomeOutcomeResponse toResponse(IncomeOutcomeDTO dto);
}
