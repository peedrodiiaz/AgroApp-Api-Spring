package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.incidencia.CreateIncidenciaRequest;
import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.error.exception.EntityNotFoundException;
import com.salesianostriana.dam.agroapp.model.*;
import com.salesianostriana.dam.agroapp.repository.IncidenciaRepository;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final MaquinaRepository maquinaRepository;

    public Page<Incidencia> getAll(Pageable pageable) {
        return incidenciaRepository.findAll(pageable);
    }

    public Incidencia findById(Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("incidencia", id));
    }

    public Incidencia create(CreateIncidenciaRequest cmd, Trabajador trabajadorActual) {

        Maquina maquina = maquinaRepository.findById(cmd.maquinaId())
                .orElseThrow(() -> new EntityNotFoundException("máquina", cmd.maquinaId()));

        boolean existeIncidenciaAbierta = incidenciaRepository.existsByMaquinaIdAndEstadoIncidenciaIn(
                cmd.maquinaId(),
                Arrays.asList(EstadoIncidencia.ABIERTA, EstadoIncidencia.EN_PROGRESO)
        );



        Trabajador logeado;
        if (trabajadorActual.getRol() == Rol.ADMIN && cmd.trabajadorId() != null) {
            logeado = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("trabajador", cmd.trabajadorId()));
        } else {
            logeado = trabajadorActual;
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

    public Incidencia update(Long id, CreateIncidenciaRequest cmd) {
        Incidencia incidencia = findById(id);

        incidencia.setTitulo(cmd.titulo());
        incidencia.setDescripcion(cmd.descripcion());
        incidencia.setPrioridad(cmd.prioridad());
        incidencia.setEstadoIncidencia(cmd.estadoIncidencia());

        if (cmd.maquinaId() != null && !cmd.maquinaId().equals(incidencia.getMaquina().getId())) {
            Maquina maquina = maquinaRepository.findById(cmd.maquinaId())
                    .orElseThrow(() -> new EntityNotFoundException("máquina", cmd.maquinaId()));
            incidencia.setMaquina(maquina);
        }

        if (cmd.trabajadorId() != null && !cmd.trabajadorId().equals(incidencia.getTrabajador().getId())) {
            Trabajador trabajador = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("trabajador", cmd.trabajadorId()));
            incidencia.setTrabajador(trabajador);
        }

        return incidenciaRepository.save(incidencia);
    }

    @Transactional
    public Incidencia cambiarEstado(Long id, EstadoIncidencia nuevoEstado) {
        Incidencia incidencia = findById(id);

        if (nuevoEstado == EstadoIncidencia.RESUELTA &&
                incidencia.getEstadoIncidencia() != EstadoIncidencia.RESUELTA) {
            incidencia.setFechaCierre(LocalDateTime.now());
        }

        incidencia.setEstadoIncidencia(nuevoEstado);
        return incidenciaRepository.save(incidencia);
    }
}
