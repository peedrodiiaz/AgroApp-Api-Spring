package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateIncidenciaRequest(
        @NotBlank
        String titulo,
        @NotBlank
        String descripcion,
        EstadoIncidencia estadoIncidencia,
        @NotNull
        Long maquinaId,
        @NotNull
        Long trabajadorId,
        Prioridad prioridad
) {
}
