package com.salesianostriana.dam.agroapp.dto.maquina;

import com.salesianostriana.dam.agroapp.model.EstadoMaquina;

import java.time.LocalDate;

public record CreateMaquinaRequest(
        String nombre,
        String modelo,
        String numeroSerie,
        LocalDate fechaCompra,
        EstadoMaquina estadoMaquina
) {
}
