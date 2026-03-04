package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.incidencia.CambiarEstadoRequest;
import com.salesianostriana.dam.agroapp.dto.incidencia.CreateIncidenciaRequest;
import com.salesianostriana.dam.agroapp.dto.incidencia.IncidenciaResponseDto;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.IncidenciaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidencias")
@RequiredArgsConstructor
@Tag(name = "Incidencias", description = "Gestión de incidencias sobre la maquinaria agrícola")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    @GetMapping
    @Operation(summary = "Listar todas las incidencias", description = "ADMIN ve todas las incidencias; TRABAJADOR solo las suyas")
    @ApiResponse(responseCode = "200", description = "Lista de incidencias",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = IncidenciaResponseDto.class))))
    public Page<IncidenciaResponseDto> getAll(
            @Parameter(description = "Parámetros de paginación: page, size, sort") Pageable pageable) {
        return incidenciaService.getAll( pageable).map(IncidenciaResponseDto::of);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener incidencia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidencia encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IncidenciaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (no es tu incidencia)", content = @Content)
    })
    public ResponseEntity<IncidenciaResponseDto> getById(
            @Parameter(description = "ID de la incidencia", example = "1") @PathVariable Long id
            ) {
        return ResponseEntity.ok(IncidenciaResponseDto.of(incidenciaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear incidencia", description = "Crea una nueva incidencia sobre una máquina")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Incidencia creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IncidenciaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Máquina o trabajador no encontrado", content = @Content)
    })
    public ResponseEntity<IncidenciaResponseDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva incidencia", required = true,
                    content = @Content(schema = @Schema(implementation = CreateIncidenciaRequest.class),
                            examples = @ExampleObject(value = """
                                    {"titulo":"Motor averiado","descripcion":"No arranca","estadoIncidencia":"ABIERTA",
                                    "maquinaId":1,"trabajadorId":2,"prioridad":"ALTA"}""")))
            @Valid @RequestBody CreateIncidenciaRequest request,
            @AuthenticationPrincipal Trabajador user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(IncidenciaResponseDto.of(incidenciaService.create(request, user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar incidencia", description = "Actualiza los datos de una incidencia existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidencia actualizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IncidenciaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<IncidenciaResponseDto> update(
            @Parameter(description = "ID de la incidencia", example = "1") @PathVariable Long id,
            @Valid @RequestBody CreateIncidenciaRequest request
         ) {
        return ResponseEntity.ok(IncidenciaResponseDto.of(incidenciaService.update(id, request)));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de incidencia", description = "Cambia el estado de una incidencia: ABIERTA, EN_PROGRESO o RESUELTA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IncidenciaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<IncidenciaResponseDto> cambiarEstado(
            @Parameter(description = "ID de la incidencia", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo estado", required = true,
                    content = @Content(schema = @Schema(implementation = CambiarEstadoRequest.class),
                            examples = @ExampleObject(value = """
                                    {"estadoIncidencia":"EN_PROGRESO"}""")))
            @Valid @RequestBody CambiarEstadoRequest request
            ) {
        return ResponseEntity.ok(
                IncidenciaResponseDto.of(incidenciaService.cambiarEstado(id, request.estadoIncidencia())));
    }

}
