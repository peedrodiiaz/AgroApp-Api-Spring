package com.salesianostriana.dam.agroapp.dto.incidencia;

public record IncidenciaResponseDto(

        Long id,
        String descripcion,
        String titulo,
        String estadoIncidencia,
        Long maquinaId,
        String maquinaNombre,
        String maquinaModelo,
        Long trabajadorId,
        String trabajadorNombre

) {
}
