package com.salesianostriana.dam.agroapp.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Schema(description = "Datos para crear una nueva máquina")
public record CreateMaquinaRequest(
        @Schema(description = "Nombre de la máquina", example = "Tractor John Deere")
        @NotBlank(message = "El nombre de la máquina es obligatorio") String nombre,

        @Schema(description = "Modelo de la máquina", example = "6130M")
        @NotBlank(message = "El modelo es obligatorio") String modelo,

        @Schema(description = "Número de serie único", example = "JD-2024-001")
        @NotBlank(message = "El número de serie es obligatorio") String numeroSerie,

        @Schema(description = "Fecha de compra (no puede ser futura)", example = "2022-03-15")
        @NotNull("No puede ser nula")
        @PastOrPresent(message = "La fecha de compra no puede ser futura") LocalDate fechaCompra
) {
}
