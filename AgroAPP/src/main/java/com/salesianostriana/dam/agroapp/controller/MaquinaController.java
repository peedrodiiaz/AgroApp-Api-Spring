package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.maquina.CambiarEstadoMaquinaDto;
import com.salesianostriana.dam.agroapp.dto.maquina.CreateMaquinaRequest;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaStatsDto;
import com.salesianostriana.dam.agroapp.dto.maquina.UpdateMaquinaDto;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.service.MaquinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maquinas")
public class MaquinaController {
    private final MaquinaService maquinaService;

    @GetMapping
    public Page<MaquinaResponse> getAll(Pageable pageable) {
        return maquinaService.findAll(pageable).map(MaquinaResponse::of);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                MaquinaResponse.of(maquinaService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaquinaResponse> create(@RequestBody CreateMaquinaRequest request) {
        Maquina maquina = maquinaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MaquinaResponse.of(maquina));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaquinaResponse> update(@PathVariable Long id,

            @RequestBody UpdateMaquinaDto dto) {
        return ResponseEntity.ok(
                MaquinaResponse.of(maquinaService.update(id, dto)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaquinaResponse> cambiarEstado(@PathVariable Long id,
            @RequestBody CambiarEstadoMaquinaDto cmd) {
        Maquina maquina = maquinaService.cambiarEstado(id, cmd.estado());
        return ResponseEntity.ok(MaquinaResponse.of(maquina));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaquinaStatsDto> getStats() {
        return ResponseEntity.ok(maquinaService.getStats());
    }

}
