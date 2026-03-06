package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.asignacion.AsignacionResponse;
import com.salesianostriana.dam.agroapp.dto.asignacion.CreateAsignacionRequest;
import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.AsignacionService;
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

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
@Tag(name = "Asignaciones", description = "Gestión de asignaciones de maquinaria a trabajadores")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @GetMapping("/activas")
    @Operation(summary = "Obtener asignaciones activas",
            description = "ADMIN ve todas las activas hoy; TRABAJADOR solo las suyas activas hoy")
    @ApiResponse(responseCode = "200", description = "Lista de asignaciones activas",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AsignacionResponse.class))))
    public ResponseEntity<List<AsignacionResponse>> getActivas(@AuthenticationPrincipal Trabajador user) {
        List<AsignacionResponse> result;
        if (user.getRol() == Rol.ADMIN) {
            result = asignacionService.getAsignacionesActivas()
                    .stream().map(AsignacionResponse::of).toList();
        } else {
            result = asignacionService.getMisAsignaciones(user)
                    .stream().map(AsignacionResponse::of).toList();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(summary = "Listar asignaciones",
            description = "ADMIN ve todas las asignaciones; TRABAJADOR solo las suyas")
    @ApiResponse(responseCode = "200", description = "Lista de asignaciones",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AsignacionResponse.class)))
    )

    public ResponseEntity<Page<AsignacionResponse>> getAll(
            @AuthenticationPrincipal Trabajador user,
            @Parameter(description = "Parámetros de paginación: page, size, sort")
            @PageableDefault(size = 10) Pageable pageable) {

        Page<AsignacionResponse> result;

        if (user.getRol() == Rol.ADMIN) {
            result = asignacionService.getAll(pageable).map(AsignacionResponse::of);
        } else {
            var lista = asignacionService.getMisAsignaciones(user)
                    .stream().map(AsignacionResponse::of).toList();
            lista = lista.reversed();
            result = new PageImpl<>(lista, pageable, lista.size());
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asignación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignación encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AsignacionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<AsignacionResponse> getById(
            @Parameter(description = "ID de la asignación", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(AsignacionResponse.of(asignacionService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear asignación",
            description = "ADMIN asigna una máquina a un trabajador para un periodo concreto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignación creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AsignacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fechas incorrectas", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Máquina o trabajador no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "La máquina ya está asignada en ese periodo", content = @Content)
    })
    public ResponseEntity<AsignacionResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva asignación", required = true,
                    content = @Content(schema = @Schema(implementation = CreateAsignacionRequest.class),
                            examples = @ExampleObject(value = """
                                    {"fechaInicio":"2024-05-01","fechaFin":"2024-05-31",
                                    "descripcion":"Siembra parcela norte","maquinaId":1,"trabajadorId":2}""")))
            @Valid @RequestBody CreateAsignacionRequest request,
            @AuthenticationPrincipal Trabajador user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AsignacionResponse.of(asignacionService.create(request, user)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar asignación", description = "Actualiza los datos de una asignación. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignación actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AsignacionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<AsignacionResponse> update(
            @Parameter(description = "ID de la asignación", example = "1") @PathVariable Long id,
            @Valid @RequestBody CreateAsignacionRequest request,
            @AuthenticationPrincipal Trabajador user) {
        return ResponseEntity.ok(AsignacionResponse.of(asignacionService.update(id, request, user)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar asignación", description = "Elimina una asignación del sistema. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Asignación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la asignación", example = "1") @PathVariable Long id) {
        asignacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
