package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.trabajador.CreateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.UpdateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.TrabajadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
@Tag(name = "Trabajadores", description = "Gestión de trabajadores del sistema")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear trabajador", description = "Crea un nuevo trabajador. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trabajador creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "409", description = "DNI, email o teléfono ya registrado", content = @Content)
    })
    public ResponseEntity<TrabajadorResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo trabajador", required = true,
                    content = @Content(schema = @Schema(implementation = CreateTrabajadorRequest.class),
                            examples = @ExampleObject(value = """
                                    {"nombre":"Juan","apellido":"García","dni":"12345678A",
                                    "email":"juan@agroapp.com","telefono":"612345678",
                                    "password":"Segura1234!","rol":"TRABAJADOR"}""")))
            @Valid @RequestBody CreateTrabajadorRequest body) {
        Trabajador nuevo = trabajadorService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(TrabajadorResponse.of(nuevo));
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil propio", description = "Devuelve los datos del trabajador autenticado")
    @ApiResponse(responseCode = "200", description = "Datos del usuario autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorResponse.class)))
    public ResponseEntity<TrabajadorResponse> getMe(@AuthenticationPrincipal Trabajador user) {
        return ResponseEntity.ok(TrabajadorResponse.of(user));
    }

    @PutMapping("/me")
    @Operation(summary = "Actualizar perfil propio", description = "El trabajador autenticado actualiza sus propios datos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<TrabajadorResponse> updateMe(
            @AuthenticationPrincipal Trabajador user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a actualizar (todos opcionales)", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateTrabajadorRequest.class),
                            examples = @ExampleObject(value = """
                                    {"nombre":"Juan","apellido":"García","telefono":"699000001","email":"nuevo@mail.com"}""")))
            @Valid @RequestBody UpdateTrabajadorRequest body) {
        return ResponseEntity.ok(TrabajadorResponse.of(trabajadorService.update(user.getId(), body)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar trabajador por ID", description = "ADMIN actualiza los datos de cualquier trabajador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<TrabajadorResponse> update(
            @Parameter(description = "ID del trabajador", example = "2") @PathVariable Long id,
            @Valid @RequestBody UpdateTrabajadorRequest body) {
        return ResponseEntity.ok(TrabajadorResponse.of(trabajadorService.update(id, body)));
    }

    @PatchMapping("/{id}/activacion")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar/desactivar trabajador", description = "Alterna el estado activo/inactivo de un trabajador. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<TrabajadorResponse> toggleActivacion(
            @Parameter(description = "ID del trabajador", example = "2") @PathVariable Long id) {
        return ResponseEntity.ok(TrabajadorResponse.of(trabajadorService.cambiarActivoInactivo(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar trabajadores", description = "Devuelve una página con todos los trabajadores. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrabajadorResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public Page<TrabajadorResponse> getAll(
            @Parameter(description = "Parámetros de paginación: page, size, sort")
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return trabajadorService.getAll(pageable).map(TrabajadorResponse::of);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar trabajador", description = "Elimina un trabajador por su ID. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Trabajador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del trabajador", example = "2") @PathVariable Long id) {
        trabajadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
