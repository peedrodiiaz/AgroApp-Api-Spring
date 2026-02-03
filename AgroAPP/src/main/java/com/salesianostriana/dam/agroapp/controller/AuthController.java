package com.salesianostriana.dam.agroapp.controller;



import com.salesianostriana.dam.agroapp.dto.security.AuthResponse;
import com.salesianostriana.dam.agroapp.dto.security.LoginRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;

import com.salesianostriana.dam.agroapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TrabajadorRepository repository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Trabajador user = repository.findByEmail(request.email())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponse(jwtToken, TrabajadorResponse.of(user))
        );
    }
}
