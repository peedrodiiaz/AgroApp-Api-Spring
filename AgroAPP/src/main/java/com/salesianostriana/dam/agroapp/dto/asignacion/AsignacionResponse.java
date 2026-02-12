package com.salesianostriana.dam.agroapp.dto.asignacion;

import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Asignacion;

import java.time.LocalDate;

public record AsignacionResponse(
        Long id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String descripcion,
        MaquinaResponse maquina,
        TrabajadorResponse trabajador
) {
    public static AsignacionResponse of(Asignacion a) {
        return new AsignacionResponse(
                a.getId(),
                a.getFechaInicio(),
                a.getFechaFin(),
                a.getDescripcion(),
                MaquinaResponse.of(a.getMaquina()),
                TrabajadorResponse.of(a.getTrabajador())
        );
    }
}
