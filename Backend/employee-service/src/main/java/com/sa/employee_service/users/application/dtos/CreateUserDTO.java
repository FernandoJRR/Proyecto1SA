package com.sa.employee_service.users.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 100, message = "El nombre de usuario no puede exceder los 100 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 255, message = "La contraseña no puede exceder los 255 caracteres")
    private String password;
}
