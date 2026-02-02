package com.salesianostriana.dam.agroapp.controller;


import com.salesianostriana.dam.agroapp.dto.trabajador.CreateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.dto.trabajador.UpdateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.service.TrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    @PostMapping
    public ResponseEntity<TrabajadorResponse> create(@Valid @RequestBody CreateTrabajadorRequest body) {
        Trabajador nuevo = trabajadorService.create(body);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TrabajadorResponse.of(nuevo));
    }

    @GetMapping("/me")
    public ResponseEntity<TrabajadorResponse> getMe(@AuthenticationPrincipal Trabajador user) {
        return ResponseEntity.ok(TrabajadorResponse.of(user));
    }

    @PutMapping("/me")
    public ResponseEntity<TrabajadorResponse> updateMe(
            @AuthenticationPrincipal Trabajador user,
            @Valid @RequestBody UpdateTrabajadorRequest body
    ) {
        return ResponseEntity.ok(
                TrabajadorResponse.of(trabajadorService.update(user.getId(), body))
        );
    }

    @PatchMapping("/{id}/activacion")
    public ResponseEntity<TrabajadorResponse> toggleActivacion(@PathVariable Long id) {
        return ResponseEntity.ok(
                TrabajadorResponse.of(trabajadorService.cambiarActivoInactivo(id))
        );
    }

    @GetMapping
    public Page<TrabajadorResponse> getAll(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return trabajadorService.getAll(pageable)
                .map(TrabajadorResponse::of);
    }


}
