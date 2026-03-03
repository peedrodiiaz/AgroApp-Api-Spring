package com.salesianostriana.dam.agroapp.error;

import com.salesianostriana.dam.agroapp.error.exception.DuplicateResourceException;
import com.salesianostriana.dam.agroapp.error.exception.EntityNotFoundException;
import com.salesianostriana.dam.agroapp.security.errorhandling.JwtException;
import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Recurso no encontrado");
        pd.setType(URI.create("https://agroapp.api/errors/not-found"));
        return pd;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResource(DuplicateResourceException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Recurso duplicado");
        pd.setType(URI.create("https://agroapp.api/errors/conflict"));
        return pd;
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción");
        pd.setTitle("Acceso denegado");
        pd.setType(URI.create("https://agroapp.api/errors/forbidden"));
        return pd;
    }


    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthentication(AuthenticationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("No autenticado");
        pd.setType(URI.create("https://agroapp.api/errors/unauthorized"));
        return ErrorResponse.builder(ex, pd)
                .header("WWW-Authenticate", "Bearer")
                .build();
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException(JwtException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("Token JWT inválido");
        pd.setType(URI.create("https://agroapp.api/errors/unauthorized"));
        return pd;
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ProblemDetail handleRefreshToken(RefreshTokenException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("Refresh token inválido o expirado");
        pd.setType(URI.create("https://agroapp.api/errors/unauthorized"));
        return pd;
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "La petición contiene campos inválidos");
        pd.setTitle("Datos de entrada inválidos");
        pd.setType(URI.create("https://agroapp.api/errors/validation"));

        return ResponseEntity.badRequest().body(pd);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Argumento inválido");
        pd.setType(URI.create("https://agroapp.api/errors/bad-request"));
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Se ha producido un error inesperado. Por favor, contacte con el administrador.");
        pd.setTitle("Error interno del servidor");
        pd.setType(URI.create("https://agroapp.api/errors/internal"));
        return pd;
    }


}
