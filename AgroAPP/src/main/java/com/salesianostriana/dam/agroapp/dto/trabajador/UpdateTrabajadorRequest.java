package com.salesianostriana.dam.agroapp.dto.trabajador;

import jakarta.validation.constraints.Email;

public record UpdateTrabajadorRequest(
        String nombre,
        String apellido,
        String telefono,
        @Email String email
) {
}
