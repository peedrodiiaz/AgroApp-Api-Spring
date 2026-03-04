package com.salesianostriana.dam.agroapp.dto.trabajador;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Datos para actualizar el perfil de un trabajador (todos opcionales)")
public record UpdateTrabajadorRequest(
        @Schema(description = "Nuevo nombre", example = "Juan") String nombre,
        @Schema(description = "Nuevo apellido", example = "García López") String apellido,
        @Schema(description = "Nuevo teléfono", example = "612345678") String telefono,
        @Schema(description = "Nuevo email", example = "juan.nuevo@agroapp.com") @Email String email
) {
}
