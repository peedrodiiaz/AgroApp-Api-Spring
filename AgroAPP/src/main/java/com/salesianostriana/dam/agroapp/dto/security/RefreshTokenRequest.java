package com.salesianostriana.dam.agroapp.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para renovar el token de acceso mediante un refresh token")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token obtenido al iniciar sesión", example = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
        String refreshToken
) {
}
