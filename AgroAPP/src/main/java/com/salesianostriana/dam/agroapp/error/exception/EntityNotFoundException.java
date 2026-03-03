package com.salesianostriana.dam.agroapp.error.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entity, Long id) {
        super("No se ha encontrado %s con id %d".formatted(entity, id));
    }
}
