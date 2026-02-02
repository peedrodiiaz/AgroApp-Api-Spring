package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.model.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.model.Prioridad;
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
