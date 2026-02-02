package com.salesianostriana.dam.agroapp.dto.trabajador;

import com.salesianostriana.dam.agroapp.model.Rol;
import com.salesianostriana.dam.agroapp.model.Trabajador;

import java.time.LocalDate;

public record TrabajadorResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String dni,
        String telefono,
        LocalDate fechaAlta,
        Rol rol
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
                t.getRol()
        );
    }
}
