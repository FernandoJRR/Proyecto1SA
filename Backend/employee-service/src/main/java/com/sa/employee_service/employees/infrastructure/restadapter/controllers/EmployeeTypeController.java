package com.sa.employee_service.employees.infrastructure.restadapter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sa.employee_service.employees.application.inputports.ForEmployeeTypePort;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.SaveEmployeeTypeRequestDTO;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.EmployeeTypeMapper;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.PermissionMapper;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;
import com.sa.employee_service.shared.infrastructure.dtos.EmployeeTypeResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee-types")

@RequiredArgsConstructor
public class EmployeeTypeController {

        private final ForEmployeeTypePort employeeTypePort;
        private final EmployeeTypeMapper employeeTypeMapper;
        private final PermissionMapper permissionMapper;

        @Operation(summary = "Obtener todos los tipos de empleados", description = "Devuelve la lista de los typos de empleados existentes.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de tipos de empleados obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<EmployeeTypeResponseDTO> getEmployeesTypes() {

                // mandar a crear el employee al port
                List<EmployeeTypeEntity> result = employeeTypePort.findAllEmployeesTypes();

                // convertir el Employee al dto
                List<EmployeeTypeResponseDTO> response = employeeTypeMapper
                                .fromEmployeeTypeListToEmployeeTypeResponseDtoList(result);

                return response;
        }

        @Operation(summary = "Obtiene un tipo de empleado por su ID", description = "Este endpoint permite obtener la información de un tipo de empleado específico utilizando su ID. Si el tipo de empleado no existe, se lanza una excepción.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tipo de empleado encontrado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - No existe un tipo de empleado con el ID especificado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{employeeTypeId}")
        @ResponseStatus(HttpStatus.OK)
        public EmployeeTypeResponseDTO finEmployeeTypeById(
                        @PathVariable("employeeTypeId") String employeeTypeId) throws NotFoundException {

                // mandar a busar por el id
                EmployeeTypeEntity result = employeeTypePort.findEmployeeTypeById(employeeTypeId);

                // convertir el employee type al dto
                EmployeeTypeResponseDTO response = employeeTypeMapper.fromEmployeeTypeToEmployeeTypeResponseDto(result);
                return response;
        }

        /*
        @Operation(summary = "Elimina un tipo de empleado por su ID", description = "Este endpoint permite eliminar un tipo de empleado del sistema. Si el tipo está asignado a empleados, estos serán reasignados al tipo de empleado por defecto antes de eliminar. No se permite eliminar el tipo de empleado por defecto.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Tipo de empleado eliminado exitosamente"),
                        @ApiResponse(responseCode = "409", description = "Conflicto - No se puede eliminar el tipo de empleado por defecto"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - No existe un tipo de empleado con el ID especificado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @DeleteMapping("/{employeeTypeId}")
        @PreAuthorize("hasAuthority('DELETE_EMPLOYEE_TYPE')")
        public ResponseEntity<EmployeeTypeResponseDTO> deleteEmployeeTypeById(
                        @PathVariable("employeeTypeId") String employeeTypeId)
                        throws NotFoundException {

                // mandar a eliminar el tipo de empleado
                employeeTypePort.deleteEmployeeTypeById(
                                employeeTypeId);

                return ResponseEntity.noContent().build();
        }
         */
}
