package com.salesianostriana.dam.agroapp.dto.maquina;

import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import com.salesianostriana.dam.agroapp.model.Maquina;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de una máquina devueltos por la API")
public record MaquinaResponse(
        @Schema(description = "ID de la máquina", example = "1") Long id,
        @Schema(description = "Nombre de la máquina", example = "Tractor John Deere") String nombre,
        @Schema(description = "Modelo", example = "6130M") String modelo,
        @Schema(description = "Número de serie (único)", example = "JD-2024-001") String numSerie,
        @Schema(description = "Fecha de compra", example = "2022-03-15") LocalDate fechaCompra,
        @Schema(description = "Estado actual: ACTIVA, MANTENIMIENTO o INACTIVA", example = "ACTIVA") EstadoMaquina estado
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
