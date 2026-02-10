package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.security.AuthResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.RefreshTokenRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final TrabajadorRepository trabajadorRepository;
  private final JwtService jwtService;

  @Value("${jwt.refresh.duration}")
  private int durationInMinutes;

  public RefreshToken create(Trabajador trabajador) {
    refreshTokenRepository.deleteByTrabajador(trabajador);
    return refreshTokenRepository.save(
        RefreshToken.builder()
            .trabajador(trabajador)
            .expireAt(Instant.now().plusSeconds(durationInMinutes * 60))
            .build());
  }

  public RefreshToken verify(RefreshToken refreshToken) {
    if (refreshToken.getExpireAt().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(refreshToken);
      throw new RefreshTokenException("Token de refresco caducado. Por favor, vuelva a loguearse");
    }
    return refreshToken;
  }

  public AuthResponse refreshToken(String token) {
    return refreshTokenRepository.findById(Long.parseLong(token))
        .map(this::verify)
        .map(RefreshToken::getTrabajador)
        .map(trabajador -> {
          String accessToken = jwtService.generateToken(trabajador);
          RefreshToken refreshedToken = this.create(trabajador);
          return new AuthResponse(accessToken, refreshedToken.getToken(), TrabajadorResponse.of(trabajador));
        })
        .orElseThrow(
            () -> new RefreshTokenException("No se ha podido refrescar el token. Por favor, vuelva a loguearse"));
  }

  public void deleteByTrabajador(Trabajador trabajador) {
    refreshTokenRepository.deleteByTrabajador(trabajador);
  }
}
