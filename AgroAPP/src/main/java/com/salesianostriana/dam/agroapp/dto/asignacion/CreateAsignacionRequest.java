package com.salesianostriana.dam.agroapp.dto.asignacion;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateAsignacionRequest(
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        String descripcion,
        @NotNull Long maquinaId,
        Long trabajadorId
) {
    // Esto lo he hecho para validar asi facil
    public void validar() {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden crear asignaciones en el pasado");
        }
    }
}
