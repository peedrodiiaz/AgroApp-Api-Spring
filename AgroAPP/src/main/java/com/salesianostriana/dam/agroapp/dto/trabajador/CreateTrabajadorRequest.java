package com.salesianostriana.dam.agroapp.dto.trabajador;

import com.salesianostriana.dam.agroapp.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrabajadorRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank String dni,
        @Email @NotBlank String email,
        @NotBlank String telefono,
        @NotBlank String password,
        @NotNull Rol rol
) {}
