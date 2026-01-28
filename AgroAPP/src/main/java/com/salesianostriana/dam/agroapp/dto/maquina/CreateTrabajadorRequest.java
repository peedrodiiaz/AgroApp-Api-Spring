package com.salesianostriana.dam.agroapp.dto.maquina;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateTrabajadorRequest(
        @NotBlank
        String nombre,
        @NotBlank
        String apellidos,
        String dni,

        @Email
        String email,
        String telefono
) {
}
