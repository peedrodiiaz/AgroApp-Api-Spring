package com.salesianostriana.dam.agroapp.error.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String field, String value) {
        super("Ya existe un registro con %s '%s'".formatted(field, value));
    }
}
