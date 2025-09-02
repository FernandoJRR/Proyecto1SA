package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeHistory;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.EmployeeHistoryDateRequestDTO;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.EmployeeHistoryResponseDTO;
import com.sa.employee_service.shared.utils.DateFormatterUtil;

@Mapper(componentModel = "spring")
public interface EmployeeHistoryMapper {
    public EmployeeHistory fromEmployeeHistoryDateRequestDtoToEmployeeHistory(EmployeeHistoryDateRequestDTO dto);

    //@Mapping(source = "historyDate", target = "historyDate", qualifiedByName = "formatDateToLocalFormat")
    @Mapping(source = "historyDate", target = "historyDate", dateFormat = "dd/MM/yyyy")
    public EmployeeHistoryResponseDTO fromEmployeeHistoryToEmployeeHistoryDto(EmployeeHistory employeeHistory);

    public List<EmployeeHistoryResponseDTO> fromEmployeeHistoriesToEmployeeHistoryDtoList(
            List<EmployeeHistory> employeeHistories);
}
