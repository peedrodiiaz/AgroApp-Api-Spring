package com.salesianostriana.dam.agroapp.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estadísticas de máquinas del sistema")
public record MaquinaStatsDto(
        @Schema(description = "Total de máquinas registradas", example = "15") long total,
        @Schema(description = "Máquinas en estado ACTIVA", example = "10") long activas,
        @Schema(description = "Máquinas en estado MANTENIMIENTO", example = "3") long mantenimiento
) {
}
