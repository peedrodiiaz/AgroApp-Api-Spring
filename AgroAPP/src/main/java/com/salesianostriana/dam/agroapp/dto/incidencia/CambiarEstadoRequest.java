package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(
        @NotNull
        EstadoIncidencia estadoIncidencia
) {
}
