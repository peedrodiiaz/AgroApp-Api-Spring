package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.asignacion.AsignacionResponse;
import com.salesianostriana.dam.agroapp.dto.asignacion.CreateAsignacionRequest;
import com.salesianostriana.dam.agroapp.model.Asignacion;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.AsignacionService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    private final AsignacionService asignacionService;


    @GetMapping
    public Page<AsignacionResponse> getAll(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return asignacionService.getAll(pageable)
                .map(AsignacionResponse::of);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionResponse> getById(@PathVariable Long id) {
        Asignacion asignacion = asignacionService.findById(id);
        return ResponseEntity.ok(AsignacionResponse.of(asignacion));
    }

    @GetMapping("/trabajador/{trabajadorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AsignacionResponse>> getByTrabajador(
            @PathVariable Long trabajadorId
    ) {
        List<AsignacionResponse> asignaciones = asignacionService
                .getByTrabajador(trabajadorId)
                .stream()
                .map(AsignacionResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(asignaciones);
    }


    @GetMapping("/maquina/{maquinaId}")
    public ResponseEntity<List<AsignacionResponse>> getByMaquina(
            @PathVariable Long maquinaId
    ) {
        List<AsignacionResponse> asignaciones = asignacionService
                .getByMaquina(maquinaId)
                .stream()
                .map(AsignacionResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(asignaciones);
    }


    @GetMapping("/activas")
    public ResponseEntity<List<AsignacionResponse>> getActivas() {
        List<AsignacionResponse> asignaciones = asignacionService
                .getAsignacionesActivas()
                .stream()
                .map(AsignacionResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(asignaciones);
    }


    @GetMapping("/mis-asignaciones")
    public ResponseEntity<List<AsignacionResponse>> getMisAsignaciones(
            @AuthenticationPrincipal Trabajador trabajadorActual
    ) {
        List<AsignacionResponse> asignaciones = asignacionService
                .getMisAsignaciones(trabajadorActual)
                .stream()
                .map(AsignacionResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(asignaciones);
    }


    @PostMapping
    public ResponseEntity<AsignacionResponse> create(
            @Valid @RequestBody CreateAsignacionRequest request,
            @AuthenticationPrincipal Trabajador trabajadorActual
    ) {
        Asignacion asignacion = asignacionService.create(request, trabajadorActual);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AsignacionResponse.of(asignacion));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AsignacionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateAsignacionRequest request,
            @AuthenticationPrincipal Trabajador trabajadorActual
    ) {
        Asignacion asignacion = asignacionService.update(id, request, trabajadorActual);
        return ResponseEntity.ok(AsignacionResponse.of(asignacion));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Trabajador trabajadorActual
    ) {
        asignacionService.delete(id, trabajadorActual);
        return ResponseEntity.noContent().build();
    }
}
