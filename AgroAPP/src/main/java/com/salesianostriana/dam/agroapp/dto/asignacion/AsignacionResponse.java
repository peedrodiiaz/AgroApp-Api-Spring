package com.salesianostriana.dam.agroapp.dto.asignacion;

import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Asignacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de una asignación devueltos por la API")
public record AsignacionResponse(
        @Schema(description = "ID de la asignación", example = "1") Long id,
        @Schema(description = "Fecha de inicio", example = "2024-04-01") LocalDate fechaInicio,
        @Schema(description = "Fecha de fin", example = "2024-04-30") LocalDate fechaFin,
        @Schema(description = "Descripción de la tarea", example = "Uso del tractor para siembra de parcela norte") String descripcion,
        @Schema(description = "Máquina asignada") MaquinaResponse maquina,
        @Schema(description = "Trabajador asignado") TrabajadorResponse trabajador
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
