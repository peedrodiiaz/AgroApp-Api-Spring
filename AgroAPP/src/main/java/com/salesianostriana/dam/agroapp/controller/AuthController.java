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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Login attempt for email: " + request.email());
        System.out.println("Password provided: " + request.password());
        
        Trabajador user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("User found: " + user.getEmail());
        System.out.println("Password hash in DB: " + user.getPassword());
        System.out.println("Password matches: " + passwordEncoder.matches(request.password(), user.getPassword()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponse(jwtToken, TrabajadorResponse.of(user))
        );
    }
}
