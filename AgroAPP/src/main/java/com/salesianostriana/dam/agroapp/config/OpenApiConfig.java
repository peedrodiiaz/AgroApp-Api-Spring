package com.salesianostriana.dam.agroapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AgroApp API",
                version = "1.0",
                description = "API REST para la gestión agrícola: maquinaria, trabajadores, asignaciones e incidencias.",
                contact = @Contact(name = "Salesianos Triana - DAM", email = "agroapp@salesianos.edu")
        ),
        servers = {
                @Server(url = "http://localhost:9000", description = "Servidor local")
        },
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        description = "Introduce el token JWT obtenido en el endpoint /api/auth/login"
)
public class OpenApiConfig {
}
