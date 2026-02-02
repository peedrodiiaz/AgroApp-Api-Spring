package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.dto.maquina.CambiarEstadoMaquinaDto;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaResponse;
import com.salesianostriana.dam.agroapp.dto.maquina.UpdateMaquinaDto;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.service.MaquinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping ("/api/maquinas")
public class MaquinaController {
    private  final MaquinaService maquinaService;


    @GetMapping
    public Page<MaquinaResponse>getAll (Pageable pageable){
        return  maquinaService.findAll(pageable).map(MaquinaResponse::of);
    }

    @PutMapping ("/{id/estado")
    public ResponseEntity<MaquinaResponse> update (@PathVariable Long id,

                                                   @RequestBody UpdateMaquinaDto dto){
        return  ResponseEntity.ok(
                MaquinaResponse.of(maquinaService.update(id, dto))
        );
    }

    @PatchMapping ("{id}/estado")
    public ResponseEntity<MaquinaResponse>cambiarEstado(@PathVariable Long id,
                                                        @RequestBody  CambiarEstadoMaquinaDto cmd)
    {
        Maquina maquina= maquinaService.cambiarEstado(id,cmd.estado());
        return ResponseEntity.ok(MaquinaResponse.of(maquina));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
