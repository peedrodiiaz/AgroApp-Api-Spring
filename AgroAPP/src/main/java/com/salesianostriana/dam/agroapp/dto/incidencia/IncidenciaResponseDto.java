package com.salesianostriana.dam.agroapp.dto.incidencia;

import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.Prioridad;
import com.salesianostriana.dam.agroapp.model.Incidencia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Datos de una incidencia devueltos por la API")
public record IncidenciaResponseDto(
        @Schema(description = "ID de la incidencia", example = "1") Long id,
        @Schema(description = "Título", example = "Motor averiado") String titulo,
        @Schema(description = "Descripción detallada", example = "El motor del tractor no arranca") String descripcion,
        @Schema(description = "Estado actual", example = "ABIERTA") EstadoIncidencia estadoIncidencia,
        @Schema(description = "Prioridad", example = "ALTA") Prioridad prioridad,
        @Schema(description = "Fecha y hora de apertura", example = "2024-03-15T09:30:00") LocalDateTime fechaApertura,
        @Schema(description = "Máquina asociada a la incidencia") MaquinaResponse maquina,
        @Schema(description = "Trabajador responsable") TrabajadorResponse trabajador,
        @Schema(description = "Latitud de la incidencia", example = "37.3886") Double latitud,
        @Schema(description = "Longitud de la incidencia", example = "-5.9823") Double longitud
) {
    public static IncidenciaResponseDto of(Incidencia i) {
        return new IncidenciaResponseDto(
                i.getId(), i.getTitulo(), i.getDescripcion(),
                i.getEstadoIncidencia(), i.getPrioridad(), i.getFechaApertura(),
                MaquinaResponse.of(i.getMaquina()), TrabajadorResponse.of(i.getTrabajador()),
                i.getLatitud(), i.getLongitud()
        );
    }
}
