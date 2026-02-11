package com.salesianostriana.dam.agroapp.security.errorhandling;

import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class JwtControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ErrorResponse handleAuthenticationException(AuthenticationException ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        ex.getMessage());

    ErrorResponse response = ErrorResponse.builder(ex, problemDetail)
        .header("WWW-Authenticate", "Bearer")
        .build();

    return response;
  }

  @ExceptionHandler(JwtException.class)
  public ProblemDetail handleJwtException(JwtException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        ex.getMessage());

    return problemDetail;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
        ex.getMessage());

    return problemDetail;
  }

  @ExceptionHandler(RefreshTokenException.class)
  public ProblemDetail handleRefreshTokenException(RefreshTokenException ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        ex.getMessage());

    return problemDetail;
  }

  @ExceptionHandler(RuntimeException.class)
  public ProblemDetail handleRuntimeException(RuntimeException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        ex.getMessage());
    return problemDetail;
  }
}
