package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.asignacion.CreateAsignacionRequest;
import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.error.exception.EntityNotFoundException;
import com.salesianostriana.dam.agroapp.model.Asignacion;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.AsignacionRepository;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final MaquinaRepository maquinaRepository;

    public Page<Asignacion> getAll(Pageable pageable) {
        return asignacionRepository.findAll(pageable);
    }

    public Asignacion findById(Long id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("asignación", id));
    }

    public List<Asignacion> getByTrabajador(Long trabajadorId) {
        if (!trabajadorRepository.existsById(trabajadorId)) {
            throw new EntityNotFoundException("trabajador", trabajadorId);
        }
        return asignacionRepository.findByTrabajadorId(trabajadorId);
    }

    public List<Asignacion> getByMaquina(Long maquinaId) {
        if (!maquinaRepository.existsById(maquinaId)) {
            throw new EntityNotFoundException("máquina", maquinaId);
        }
        return asignacionRepository.findByMaquinaId(maquinaId);
    }

    public List<Asignacion> getAsignacionesActivas() {
        return asignacionRepository.findAsignacionesActivas(LocalDate.now());
    }

    public List<Asignacion> getMisAsignaciones(Trabajador trabajadorActual) {
        return asignacionRepository.findByTrabajadorId(trabajadorActual.getId());
    }

    public Asignacion create(CreateAsignacionRequest cmd, Trabajador trabajadorActual) {
        cmd.validar();

        Maquina maquina = maquinaRepository.findById(cmd.maquinaId())
                .orElseThrow(() -> new EntityNotFoundException("máquina", cmd.maquinaId()));



        Trabajador trabajadorAsignado;
        if (trabajadorActual.getRol() == Rol.ADMIN && cmd.trabajadorId() != null) {
            trabajadorAsignado = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("trabajador", cmd.trabajadorId()));
        } else {
            trabajadorAsignado = trabajadorActual;
        }

        boolean haySolapamiento = asignacionRepository.existsSolapamiento(
                cmd.maquinaId(), cmd.fechaInicio(), cmd.fechaFin());



        Asignacion asignacion = Asignacion.builder()
                .fechaInicio(cmd.fechaInicio())
                .fechaFin(cmd.fechaFin())
                .descripcion(cmd.descripcion())
                .trabajador(trabajadorAsignado)
                .maquina(maquina)
                .build();

        return asignacionRepository.save(asignacion);
    }

    public Asignacion update(Long id, CreateAsignacionRequest cmd, Trabajador trabajadorActual) {
        cmd.validar();

        Asignacion asignacion = findById(id);


        if (!asignacion.getMaquina().getId().equals(cmd.maquinaId())) {
            Maquina nuevaMaquina = maquinaRepository.findById(cmd.maquinaId())
                    .orElseThrow(() -> new EntityNotFoundException("máquina", cmd.maquinaId()));


            asignacion.setMaquina(nuevaMaquina);
        }

        boolean haySolapamiento = asignacionRepository.existsSolapamientoExceptoId(
                cmd.maquinaId(), id, cmd.fechaInicio(), cmd.fechaFin());


        asignacion.setFechaInicio(cmd.fechaInicio());
        asignacion.setFechaFin(cmd.fechaFin());
        asignacion.setDescripcion(cmd.descripcion());

        if (trabajadorActual.getRol() == Rol.ADMIN && cmd.trabajadorId() != null) {
            Trabajador nuevoTrabajador = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("trabajador", cmd.trabajadorId()));
            asignacion.setTrabajador(nuevoTrabajador);
        }

        return asignacionRepository.save(asignacion);
    }

    public void delete(Long id, Trabajador trabajadorActual) {
        Asignacion asignacion = findById(id);


        asignacionRepository.delete(asignacion);
    }
}
