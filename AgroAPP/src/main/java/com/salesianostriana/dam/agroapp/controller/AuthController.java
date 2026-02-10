package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.security.AuthResponse;
import com.salesianostriana.dam.agroapp.dto.security.LoginRequest;
import com.salesianostriana.dam.agroapp.dto.security.RefreshTokenRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.agroapp.service.JwtService;
import com.salesianostriana.dam.agroapp.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        Trabajador trabajador = (Trabajador) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(trabajador);
        RefreshToken refreshToken = refreshTokenService.create(trabajador);

        return ResponseEntity.ok(
                new AuthResponse(accessToken, refreshToken.getToken(), TrabajadorResponse.of(trabajador)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
