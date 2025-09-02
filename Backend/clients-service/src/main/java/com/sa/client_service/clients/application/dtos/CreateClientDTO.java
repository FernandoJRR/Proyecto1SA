package com.sa.client_service.clients.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateClientDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido del cliente es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico inválido")
    @Size(max = 254, message = "El correo no puede tener más de 254 caracteres")
    private String email;

    @NotBlank(message = "El CUI es obligatorio")
    @Pattern(regexp = "^[0-9]{13}$", message = "El CUI debe ser un numero entero de 13 digitos")
    private String cui;
}
