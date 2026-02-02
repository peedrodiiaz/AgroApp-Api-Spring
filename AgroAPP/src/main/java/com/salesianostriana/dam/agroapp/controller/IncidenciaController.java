package com.salesianostriana.dam.agroapp.controller;

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
        // Convertimos la lista de entidades a lista de DTOs
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

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<IncidenciaResponseDto> close(@PathVariable Long id) {
        Incidencia cerrada = incidenciaService.cerrar(id);
        return ResponseEntity.ok(IncidenciaResponseDto.of(cerrada));
    }




}
