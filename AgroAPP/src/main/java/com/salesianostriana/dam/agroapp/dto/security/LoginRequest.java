package com.salesianostriana.dam.agroapp.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para iniciar sesión")
public record LoginRequest(
        @Schema(description = "Email del trabajador", example = "admin@agroapp.com") String email,
        @Schema(description = "Contraseña del trabajador", example = "Admin1234!") String password
) {
}
