package com.salesianostriana.dam.agroapp.dto.trabajador;

import com.salesianostriana.dam.agroapp.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para crear un nuevo trabajador")
public record CreateTrabajadorRequest(
        @Schema(description = "Nombre del trabajador", example = "Juan") @NotBlank String nombre,
        @Schema(description = "Apellido del trabajador", example = "García López") @NotBlank String apellido,
        @Schema(description = "DNI (único)", example = "12345678A") @NotBlank String dni,
        @Schema(description = "Email (único)", example = "juan.garcia@agroapp.com") @Email @NotBlank String email,
        @Schema(description = "Teléfono (único)", example = "612345678") @NotBlank String telefono,
        @Schema(description = "Contraseña (mínimo 8 caracteres)", example = "Segura1234!") @NotBlank String password,
        @Schema(description = "Rol: ADMIN o TRABAJADOR", example = "TRABAJADOR") @NotNull Rol rol
) {}
