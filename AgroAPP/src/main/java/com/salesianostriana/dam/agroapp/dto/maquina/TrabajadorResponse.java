package com.salesianostriana.dam.agroapp.dto.maquina;

public record TrabajadorResponse(
        Long id,
        String nombre,
        String apellidos,
        String dni,
        String email,
        String telefono
) {
}
