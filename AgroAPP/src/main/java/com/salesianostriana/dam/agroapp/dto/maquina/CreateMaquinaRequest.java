package com.salesianostriana.dam.agroapp.dto.maquina;

import com.salesianostriana.dam.agroapp.model.EstadoMaquina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record CreateMaquinaRequest(
        @NotBlank(message = "El nombre de la máquina es obligatorio")
        String nombre,

        @NotBlank(message = "El modelo es obligatorio")
        String modelo,

        @NotBlank(message = "El número de serie es obligatorio")
        String numeroSerie,

        @NotNull("No puede ser nula")
        @PastOrPresent(message = "La fecha de compra no puede ser futura")
        LocalDate fechaCompra
) {
}
