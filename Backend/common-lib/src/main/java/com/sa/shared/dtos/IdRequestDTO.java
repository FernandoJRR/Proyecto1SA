package com.sa.shared.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IdRequestDTO {

    @NotBlank(message = "El id no puede estar vacio.")
    private String id;
}
