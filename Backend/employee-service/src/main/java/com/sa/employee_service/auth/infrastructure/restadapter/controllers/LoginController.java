package com.sa.employee_service.auth.infrastructure.restadapter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.employee_service.auth.application.dtos.LoginDTO;
import com.sa.employee_service.auth.application.inputports.LoginInputPort;
import com.sa.employee_service.auth.infrastructure.restadapter.dtos.LoginRequestDTO;
import com.sa.employee_service.auth.infrastructure.restadapter.dtos.LoginResponse;
import com.sa.employee_service.auth.infrastructure.restadapter.mappers.LoginRestMapper;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginInputPort loginInputPort;
    private final LoginRestMapper loginRestMapper;

    @Operation(summary = "Permite autenticar al usuario.", description = "Este endpoint valida las credenciales y lo autentica con un jwt.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok, el usuario fue autenticado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No encontrado - Las credenciales fallaron o el nombre de usuario no existe.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequestDTO request) throws NotFoundException {
        LoginDTO result = loginInputPort.login(request.getUsername(), request.getPassword());
        LoginResponse response = loginRestMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
