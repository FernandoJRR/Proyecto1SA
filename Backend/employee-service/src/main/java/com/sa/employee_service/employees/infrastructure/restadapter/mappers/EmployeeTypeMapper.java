package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.SaveEmployeeTypeRequestDTO;
import com.sa.shared.dtos.IdRequestDTO;
import com.sa.sharedEmployeeService.dto.EmployeeTypeResponseDTO;

@Mapper(componentModel = "spring")
public interface EmployeeTypeMapper {

    public EmployeeTypeEntity fromIdRequestDtoToEmployeeType(IdRequestDTO idRequestDTO);

    public EmployeeTypeEntity fromCreateEmployeeTypeDtoToEmployeeType(SaveEmployeeTypeRequestDTO dto);

    public EmployeeTypeResponseDTO fromEmployeeTypeToEmployeeTypeResponseDto(EmployeeTypeEntity employeeType);

    public List<EmployeeTypeResponseDTO> fromEmployeeTypeListToEmployeeTypeResponseDtoList(List<EmployeeTypeEntity> types);
}
