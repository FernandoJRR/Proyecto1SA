package com.sa.employee_service.employees.infrastructure.restadapter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.inputports.CreateEmployeeInputPort;
import com.sa.employee_service.employees.application.inputports.FindAllEmployeesInputPort;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByIdInputPort;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByUsernameInputPort;
import com.sa.employee_service.employees.application.inputports.FindEmployeesByTypeInputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.CreateEmployeeRequest;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.EmployeeResponseMapper;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.EmployeeRestMapper;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;
import com.sa.employee_service.shared.infrastructure.dtos.EmployeeResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")

@RequiredArgsConstructor
public class EmployeesController {

    private final CreateEmployeeInputPort createEmployeeInputPort;
    private final FindEmployeeByIdInputPort findEmployeeByIdInputPort;
    private final FindAllEmployeesInputPort findAllEmployeesInputPort;
    private final FindEmployeeByUsernameInputPort findEmployeeByUsernameInputPort;

    private final EmployeeRestMapper employeeRestMapper;
    private final EmployeeResponseMapper employeeResponseMapper;

    @Operation(summary = "Crear un nuevo empleado", description = "Este endpoint permite la creación de un nuevo empleado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflicto - Username duplicado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No encontrado - Tipo de empleado no existe", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @RequestBody CreateEmployeeRequest request)
            throws DuplicatedEntryException, NotFoundException, InvalidParameterException {

        CreateEmployeeDTO createEmployeeDTO = employeeRestMapper.toDTO(request);

        Employee result = createEmployeeInputPort.handle(createEmployeeDTO);

        EmployeeResponseDTO response = employeeResponseMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
    @Operation(summary = "Edita un empleado", description = "Este endpoint permite la edición de un empleado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado editado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Recursos no econtrados, el usuario a editar no existe o el tipo de empleado no existe.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    })
    @PreAuthorize("hasAuthority('EDIT_EMPLOYEE')")
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable("employeeId") String employeeId,
            @RequestBody @Valid EmployeeRequestDTO request)
            throws NotFoundException {

        // extraer los parametros para la creacion del employee
        EmployeeEntity newEmployee = employeeMapper.fromEmployeeRequestDtoToEmployee(request);
        EmployeeTypeEntity employeeType = employeeTypeMapper
                .fromIdRequestDtoToEmployeeType(request.getEmployeeTypeId());

        // mandar a editar el employee al port
        EmployeeEntity result = employeesPort.updateEmployee(employeeId, newEmployee, employeeType);

        // convertir el Employee al dto
        EmployeeResponseDTO response = employeeMapper.fromEmployeeToResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    */

    @Operation(summary = "Busca un empleado", description = "Este endpoint permite la busqueda de un empleado en base a su Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> findEmployeeById(
            @PathVariable("employeeId") String employeeId)
            throws NotFoundException {

        Employee result = findEmployeeByIdInputPort.handle(employeeId);

        EmployeeResponseDTO response = employeeResponseMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener todos los empleados",
    description = "Este endpoint permite la busqueda de todos los empleados.",
    parameters = {
        @Parameter(
            name = "type",
            description = "UUID del tipo de empleado. Si se omite, retorna todos los empleados.",
            example = "2693a828-7389-44d1-9b33-b56274f6d3fb",
            schema = @Schema(type = "string", format = "uuid")
        )
    }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<EmployeeResponseDTO>> findEmployees(
        @RequestParam(name = "type", required = false) String employeeTypeId,
        @RequestParam(required = false) String establishmentId
        )
        throws NotFoundException {

        List<Employee> result = findAllEmployeesInputPort.handle(employeeTypeId, establishmentId);

        List<EmployeeResponseDTO> response = employeeResponseMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener un empleado por su nombre de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/by-username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeResponseDTO> findEmployeeByUserName(@PathVariable String username) throws NotFoundException {
        Employee result = findEmployeeByUsernameInputPort.handle(username);

        EmployeeResponseDTO response = employeeResponseMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
