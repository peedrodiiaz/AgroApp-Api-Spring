package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.incidencia.CreateIncidenciaRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.TrabajadorResponse;
import com.salesianostriana.dam.agroapp.model.*;
import com.salesianostriana.dam.agroapp.repository.IncidenciaRepository;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidenciaService {
    private  final IncidenciaRepository incidenciaRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final MaquinaRepository maquinaRepository;


    public Page <Incidencia>getAll (Pageable pageable){
        return incidenciaRepository.findAll(pageable);
    }
    public Incidencia findById (Long id){
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado incidencia con id %d".formatted(id)));
    }

    public Incidencia create (CreateIncidenciaRequest cmd, Trabajador trabajadorActual){

        Maquina maquina = maquinaRepository.findById(cmd.maquinaId()).orElseThrow(
                ()-> new EntityNotFoundException("No se ha encontrado la máquina con id %d".formatted(cmd.maquinaId()))
        );
        Trabajador logeado;
        if (trabajadorActual.getRol()== Rol.ADMIN && cmd.trabajadorId()!=null){
            logeado = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Trabajador logeado no encontrado"));
        }else {
            logeado=trabajadorActual;
        }

        Incidencia incidencia = Incidencia.builder()
                .titulo(cmd.titulo())
                .descripcion(cmd.descripcion())
                .prioridad(cmd.prioridad())
                .estadoIncidencia(cmd.estadoIncidencia())
                .fechaApertura(LocalDateTime.now())
                .build();

        incidencia.setMaquina(maquina);
        incidencia.setTrabajador(logeado);

        return incidenciaRepository.save(incidencia);

    }
    @Transactional
    public Incidencia cerrar(Long id) {
        Incidencia incidencia = findById(id);

        if (incidencia.getEstadoIncidencia() == EstadoIncidencia.RESUELTA) {
            throw new RuntimeException("La incidencia ya está resuelta");
        }

        incidencia.setEstadoIncidencia(EstadoIncidencia.RESUELTA);
        incidencia.setFechaCierre(LocalDateTime.now());

        return incidenciaRepository.save(incidencia);
    }
}

