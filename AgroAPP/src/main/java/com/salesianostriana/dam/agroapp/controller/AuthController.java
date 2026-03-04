package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.security.AuthResponse;
import com.salesianostriana.dam.agroapp.dto.security.LoginRequest;
import com.salesianostriana.dam.agroapp.dto.security.RefreshTokenRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.agroapp.service.JwtService;
import com.salesianostriana.dam.agroapp.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Endpoints para login y renovación de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña y devuelve tokens JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {"token":"eyJhbGci...","refreshToken":"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
                                    "user":{"id":1,"nombre":"Admin","apellido":"Sistema","email":"admin@agroapp.com","rol":"ADMIN"}}"""))),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de acceso",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                                    {"email":"admin@agroapp.com","password":"Admin1234!"}""")))
            @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Trabajador trabajador = (Trabajador) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(trabajador);
        RefreshToken refreshToken = refreshTokenService.create(trabajador);

        return ResponseEntity.ok(
                new AuthResponse(accessToken, refreshToken.getToken(), TrabajadorResponse.of(trabajador)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Obtiene un nuevo access token usando un refresh token válido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado", content = @Content)
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = """
                                    {"refreshToken":"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"}""")))
            @RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
