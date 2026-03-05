package com.salesianostriana.dam.agroapp.dto.trabajador;

import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de un trabajador devueltos por la API")
public record TrabajadorResponse(
        @Schema(description = "ID del trabajador", example = "1") Long id,
        @Schema(description = "Nombre", example = "Juan") String nombre,
        @Schema(description = "Apellido", example = "García López") String apellido,
        @Schema(description = "Email", example = "juan.garcia@agroapp.com") String email,
        @Schema(description = "DNI", example = "12345678A") String dni,
        @Schema(description = "Teléfono", example = "612345678") String telefono,
        @Schema(description = "Fecha de alta en el sistema", example = "2024-01-15") LocalDate fechaAlta,
        @Schema(description = "Rol del trabajador", example = "TRABAJADOR") Rol rol,
        @Schema(description = "Si el trabajador está activo", example = "true") boolean enabled
) {
    public static TrabajadorResponse of(Trabajador t) {
        return new TrabajadorResponse(
                t.getId(),
                t.getNombre(),
                t.getApellido(),
                t.getEmail(),
                t.getDni(),
                t.getTelefono(),
                t.getFechaAlta(),
                t.getRol(),
                t.isEnabled()
        );
    }
}
