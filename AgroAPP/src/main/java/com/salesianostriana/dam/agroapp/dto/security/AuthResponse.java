package com.salesianostriana.dam.agroapp.dto.security;

import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;

public record AuthResponse(String token, TrabajadorResponse user) {
}
