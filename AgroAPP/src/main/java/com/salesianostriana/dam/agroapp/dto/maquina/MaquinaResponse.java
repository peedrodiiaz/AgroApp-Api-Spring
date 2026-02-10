package com.salesianostriana.dam.agroapp.dto.maquina;

import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import com.salesianostriana.dam.agroapp.model.Maquina;

import java.time.LocalDate;

public record MaquinaResponse(
        Long id,
        String nombre,
        String modelo,
        String numSerie,
        LocalDate fechaCompra,
        EstadoMaquina estado
) {

    public static MaquinaResponse of(Maquina m){
        return new MaquinaResponse(
                m.getId(),
                m.getNombre(),
                m.getModelo(),
                m.getNumSerie(),
                m.getFechaCompra(),
                m.getEstado()
        );
    }
}
