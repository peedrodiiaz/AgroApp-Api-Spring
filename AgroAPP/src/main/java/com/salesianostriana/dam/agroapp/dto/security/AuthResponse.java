package com.salesianostriana.dam.agroapp.dto.security;

import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con tokens JWT y datos del usuario")
public record AuthResponse(
        @Schema(description = "Token de acceso JWT (válido 24h)", example = "eyJhbGci...") String token,
        @Schema(description = "Token de refresco (válido 7 días)", example = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx") String refreshToken,
        @Schema(description = "Datos del trabajador autenticado") TrabajadorResponse user
) {
}
