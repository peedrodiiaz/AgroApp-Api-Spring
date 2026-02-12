package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.incidencia.CambiarEstadoRequest;
import com.salesianostriana.dam.agroapp.dto.incidencia.CreateIncidenciaRequest;
import com.salesianostriana.dam.agroapp.dto.incidencia.IncidenciaResponseDto;
import com.salesianostriana.dam.agroapp.model.Incidencia;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.IncidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidencias")
public class IncidenciaController {
    private final IncidenciaService incidenciaService;


    @GetMapping
    public Page<IncidenciaResponseDto> getAll(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return incidenciaService.getAll(pageable)
                .map(IncidenciaResponseDto::of);
    }


    @GetMapping ("{id}")
    public ResponseEntity<IncidenciaResponseDto>getById (@PathVariable Long id){
        return ResponseEntity.ok(IncidenciaResponseDto.of(incidenciaService.findById(id)));
    }

    @PostMapping
    public  ResponseEntity<IncidenciaResponseDto>create (@RequestBody CreateIncidenciaRequest cmd ,
    @AuthenticationPrincipal Trabajador usuarioLogeado)
    {
        IncidenciaResponseDto incidenciaCreada = IncidenciaResponseDto.of(
                incidenciaService.create(cmd, usuarioLogeado)
        );
        return ResponseEntity.ok(incidenciaCreada);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IncidenciaResponseDto> update(
            @PathVariable Long id,
            @RequestBody CreateIncidenciaRequest cmd) {
        Incidencia actualizada = incidenciaService.update(id, cmd);

        return ResponseEntity.ok(IncidenciaResponseDto.of(actualizada));
    }
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IncidenciaResponseDto> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoRequest cmd) {
        Incidencia actualizada = incidenciaService.cambiarEstado(id, cmd.estadoIncidencia());
        return ResponseEntity.ok(IncidenciaResponseDto.of(actualizada));
    }





}
