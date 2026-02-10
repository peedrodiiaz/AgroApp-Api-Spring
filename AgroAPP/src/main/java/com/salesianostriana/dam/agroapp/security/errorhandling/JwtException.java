package com.salesianostriana.dam.agroapp.security.errorhandling;

public class JwtException extends RuntimeException {
  public JwtException(String message) {
    super(message);
  }
}
