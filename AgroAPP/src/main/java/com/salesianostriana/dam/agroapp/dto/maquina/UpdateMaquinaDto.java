package com.salesianostriana.dam.agroapp.dto.maquina;

import java.time.LocalDate;

public record UpdateMaquinaDto (
    String nombre,
    String modelo,
    LocalDate fechaCompra
){}
