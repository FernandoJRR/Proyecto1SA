package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.HistoryType;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.HistoryTypeResponseDTO;
import com.sa.shared.dtos.IdRequestDTO;

@Mapper(componentModel = "spring")
public interface HistoryTypeMapper {
    HistoryTypeResponseDTO fromHistoryTypeToHistoryTypeResponseDTO(HistoryType historyType);
    HistoryType fromHistoryTypeDtoToHistoryType(HistoryTypeResponseDTO historyTypeResponseDTO);
    HistoryType fromIdRequestDtoToHistoryType(IdRequestDTO idRequestDTO);
    List<HistoryTypeResponseDTO> fromHistoryTypesToHistoryTypeResponseDTOs(List<HistoryType> historyTypes);
}
