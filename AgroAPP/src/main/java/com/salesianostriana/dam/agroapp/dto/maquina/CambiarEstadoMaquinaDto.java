package com.salesianostriana.dam.agroapp.dto.maquina;

import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para cambiar el estado de una máquina")
public record CambiarEstadoMaquinaDto(
        @Schema(description = "Nuevo estado: ACTIVA, MANTENIMIENTO o INACTIVA", example = "MANTENIMIENTO")
        EstadoMaquina estado
) {
}
