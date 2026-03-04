package com.salesianostriana.dam.agroapp.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Datos para actualizar una máquina (todos opcionales)")
public record UpdateMaquinaDto(
        @Schema(description = "Nuevo nombre", example = "Tractor New Holland") String nombre,
        @Schema(description = "Nuevo modelo", example = "T7.210") String modelo,
        @Schema(description = "Nueva fecha de compra", example = "2023-06-01") LocalDate fechaCompra
) {}
