package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Petición para cambiar el estado de una incidencia")
public record CambiarEstadoRequest(
        @Schema(description = "Nuevo estado: ABIERTA, EN_PROGRESO o RESUELTA", example = "EN_PROGRESO")
        @NotNull EstadoIncidencia estadoIncidencia
) {
}
