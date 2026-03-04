package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.maquina.CambiarEstadoMaquinaDto;
import com.salesianostriana.dam.agroapp.dto.maquina.CreateMaquinaRequest;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaStatsDto;
import com.salesianostriana.dam.agroapp.dto.maquina.UpdateMaquinaDto;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.service.MaquinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maquinas")
@Tag(name = "Máquinas", description = "Gestión del inventario de maquinaria agrícola")
public class MaquinaController {

    private final MaquinaService maquinaService;

    @GetMapping
    @Operation(summary = "Listar máquinas", description = "Devuelve una página con todas las máquinas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de máquinas",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MaquinaResponse.class))))
    public Page<MaquinaResponse> getAll(
            @Parameter(description = "Parámetros de paginación: page, size, sort") Pageable pageable) {
        return maquinaService.findAll(pageable).map(MaquinaResponse::of);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener máquina por ID", description = "Devuelve los detalles de una máquina específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Máquina encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaquinaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Máquina no encontrada", content = @Content)
    })
    public ResponseEntity<MaquinaResponse> getById(
            @Parameter(description = "ID de la máquina", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(MaquinaResponse.of(maquinaService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear máquina", description = "Registra una nueva máquina en el sistema. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Máquina creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaquinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Número de serie ya registrado", content = @Content)
    })
    public ResponseEntity<MaquinaResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva máquina", required = true,
                    content = @Content(schema = @Schema(implementation = CreateMaquinaRequest.class),
                            examples = @ExampleObject(value = """
                                    {"nombre":"Tractor John Deere","modelo":"6130M","numeroSerie":"JD-2024-001","fechaCompra":"2022-03-15"}""")))
            @RequestBody CreateMaquinaRequest request) {
        Maquina maquina = maquinaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MaquinaResponse.of(maquina));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar máquina", description = "Actualiza los datos de una máquina existente. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Máquina actualizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaquinaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Máquina no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<MaquinaResponse> update(
            @Parameter(description = "ID de la máquina", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a actualizar (todos opcionales)", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMaquinaDto.class),
                            examples = @ExampleObject(value = """
                                    {"nombre":"Tractor New Holland","modelo":"T7.210","fechaCompra":"2023-06-01"}""")))
            @RequestBody UpdateMaquinaDto dto) {
        return ResponseEntity.ok(MaquinaResponse.of(maquinaService.update(id, dto)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado de máquina", description = "Cambia el estado de una máquina: ACTIVA, MANTENIMIENTO o INACTIVA. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaquinaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Máquina no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Transición de estado inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<MaquinaResponse> cambiarEstado(
            @Parameter(description = "ID de la máquina", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo estado", required = true,
                    content = @Content(schema = @Schema(implementation = CambiarEstadoMaquinaDto.class),
                            examples = @ExampleObject(value = """
                                    {"estado":"MANTENIMIENTO"}""")))
            @RequestBody CambiarEstadoMaquinaDto cmd) {
        Maquina maquina = maquinaService.cambiarEstado(id, cmd.estado());
        return ResponseEntity.ok(MaquinaResponse.of(maquina));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar máquina", description = "Elimina una máquina del sistema. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Máquina eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Máquina no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la máquina", example = "1") @PathVariable Long id) {
        maquinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Estadísticas de máquinas", description = "Devuelve el total de máquinas y el conteo por estado. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaquinaStatsDto.class),
                            examples = @ExampleObject(value = """
                                    {"total":15,"activas":10,"mantenimiento":3}"""))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<MaquinaStatsDto> getStats() {
        return ResponseEntity.ok(maquinaService.getStats());
    }
}
