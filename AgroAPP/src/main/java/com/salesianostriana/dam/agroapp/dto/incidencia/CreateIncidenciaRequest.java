package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.Prioridad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para crear o actualizar una incidencia")
public record CreateIncidenciaRequest(
        @Schema(description = "Título de la incidencia", example = "Motor averiado") @NotBlank String titulo,
        @Schema(description = "Descripción detallada", example = "El motor del tractor no arranca desde esta mañana") @NotBlank String descripcion,
        @Schema(description = "Estado inicial: ABIERTA, EN_PROGRESO o RESUELTA", example = "ABIERTA") EstadoIncidencia estadoIncidencia,
        @Schema(description = "ID de la máquina afectada", example = "3") @NotNull Long maquinaId,
        @Schema(description = "ID del trabajador responsable", example = "2") @NotNull Long trabajadorId,
        @Schema(description = "Prioridad: BAJA, MEDIA o ALTA", example = "ALTA") Prioridad prioridad,
        @Schema(description = "Latitud donde ocurrió la incidencia", example = "37.3886") Double latitud,
        @Schema(description = "Longitud donde ocurrió la incidencia", example = "-5.9823") Double longitud
) {
}
