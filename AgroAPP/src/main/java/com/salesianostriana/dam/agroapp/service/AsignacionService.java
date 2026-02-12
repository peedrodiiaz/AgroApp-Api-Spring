package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.asignacion.CreateAsignacionRequest;
import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.model.Asignacion;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.AsignacionRepository;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
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

    /**
     * Obtener una asignación por ID
     */
    public Asignacion findById(Long id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se ha encontrado asignación con id %d".formatted(id)
                ));
    }


    public List<Asignacion> getByTrabajador(Long trabajadorId) {
        if (!trabajadorRepository.existsById(trabajadorId)) {
            throw new EntityNotFoundException("Trabajador no encontrado");
        }
        return asignacionRepository.findByTrabajadorId(trabajadorId);
    }


    public List<Asignacion> getByMaquina(Long maquinaId) {
        if (!maquinaRepository.existsById(maquinaId)) {
            throw new EntityNotFoundException("Máquina no encontrada");
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se ha encontrado la máquina con id %d".formatted(cmd.maquinaId())
                ));

        if (maquina.getEstado() != EstadoMaquina.ACTIVA) {
            throw new RuntimeException("La máquina está desactivada y no se puede asignar");
        }

        Trabajador trabajadorAsignado;
        if (trabajadorActual.getRol() == Rol.ADMIN && cmd.trabajadorId() != null) {
            trabajadorAsignado = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));
        } else {
            trabajadorAsignado = trabajadorActual;
        }

        boolean haySolapamiento = asignacionRepository.existsSolapamiento(
                cmd.maquinaId(),
                cmd.fechaInicio(),
                cmd.fechaFin()
        );

        if (haySolapamiento) {
            throw new RuntimeException(
                    "La máquina ya está reservada para ese período de fechas. " +
                            "Por favor, elige otras fechas."
            );
        }

        Asignacion asignacion = Asignacion.builder()
                .fechaInicio(cmd.fechaInicio())
                .fechaFin(cmd.fechaFin())
                .descripcion(cmd.descripcion())
                .trabajador(trabajadorAsignado)
                .maquina(maquina)
                .build();

        return asignacionRepository.save(asignacion);
    }

    /**
     * Actualizar una asignación existente
     */
    public Asignacion update(Long id, CreateAsignacionRequest cmd, Trabajador trabajadorActual) {
        cmd.validar();

        Asignacion asignacion = findById(id);

        if (trabajadorActual.getRol() != Rol.ADMIN &&
                !asignacion.getTrabajador().getId().equals(trabajadorActual.getId())) {
            throw new RuntimeException("No tienes permisos para editar esta asignación");
        }

        if (!asignacion.getMaquina().getId().equals(cmd.maquinaId())) {
            Maquina nuevaMaquina = maquinaRepository.findById(cmd.maquinaId())
                    .orElseThrow(() -> new EntityNotFoundException("Máquina no encontrada"));

            if (nuevaMaquina.getEstado() != EstadoMaquina.ACTIVA) {
                throw new RuntimeException("La máquina está desactivada");
            }

            asignacion.setMaquina(nuevaMaquina);
        }

        boolean haySolapamiento = asignacionRepository.existsSolapamientoExceptoId(
                cmd.maquinaId(),
                id,
                cmd.fechaInicio(),
                cmd.fechaFin()
        );

        if (haySolapamiento) {
            throw new RuntimeException(
                    "La máquina ya está reservada para ese período de fechas"
            );
        }

        asignacion.setFechaInicio(cmd.fechaInicio());
        asignacion.setFechaFin(cmd.fechaFin());
        asignacion.setDescripcion(cmd.descripcion());

        if (trabajadorActual.getRol() == Rol.ADMIN && cmd.trabajadorId() != null) {
            Trabajador nuevoTrabajador = trabajadorRepository.findById(cmd.trabajadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));
            asignacion.setTrabajador(nuevoTrabajador);
        }

        return asignacionRepository.save(asignacion);
    }



    public void delete(Long id, Trabajador trabajadorActual) {
        Asignacion asignacion = findById(id);

        if (trabajadorActual.getRol() != Rol.ADMIN &&
                !asignacion.getTrabajador().getId().equals(trabajadorActual.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar esta asignación");
        }

        if (asignacion.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "No se pueden eliminar asignaciones que ya comenzaron"
            );
        }

        asignacionRepository.delete(asignacion);
    }
}
