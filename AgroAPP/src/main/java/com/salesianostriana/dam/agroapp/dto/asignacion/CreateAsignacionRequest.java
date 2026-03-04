package com.salesianostriana.dam.agroapp.dto.asignacion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Datos para crear o actualizar una asignación de máquina a trabajador")
public record CreateAsignacionRequest(
        @Schema(description = "Fecha de inicio de la asignación", example = "2024-04-01") @NotNull LocalDate fechaInicio,
        @Schema(description = "Fecha de fin de la asignación", example = "2024-04-30") @NotNull LocalDate fechaFin,
        @Schema(description = "Descripción de la tarea asignada", example = "Uso del tractor para siembra de parcela norte") String descripcion,
        @Schema(description = "ID de la máquina a asignar", example = "1") @NotNull Long maquinaId,
        @Schema(description = "ID del trabajador al que se asigna (si es null se asigna al usuario autenticado)", example = "2") Long trabajadorId
) {
    public void validar() {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden crear asignaciones en el pasado");
        }
    }
}
