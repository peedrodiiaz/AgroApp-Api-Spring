package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.model.Incidencia;
import com.salesianostriana.dam.agroapp.enums.Prioridad;

import java.time.LocalDateTime;

public record IncidenciaResponseDto(
        Long id,
        String titulo,
        String descripcion,
        EstadoIncidencia estado,
        Prioridad prioridad,
        LocalDateTime fechaApertura,
        MaquinaResponse maquina,
        TrabajadorResponse trabajador
) {
    public static  IncidenciaResponseDto of (Incidencia i){
        return  new IncidenciaResponseDto(
                i.getId(),
                i.getTitulo(),
                i.getDescripcion(),
                i.getEstadoIncidencia(),
                i.getPrioridad(),
                i.getFechaApertura(),
                MaquinaResponse.of(i.getMaquina()),
                TrabajadorResponse.of(i.getTrabajador())


        );
    }
}
