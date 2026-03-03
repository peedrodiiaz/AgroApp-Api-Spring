package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.maquina.CreateMaquinaRequest;
import com.salesianostriana.dam.agroapp.dto.maquina.MaquinaStatsDto;
import com.salesianostriana.dam.agroapp.dto.maquina.UpdateMaquinaDto;
import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import com.salesianostriana.dam.agroapp.error.exception.DuplicateResourceException;
import com.salesianostriana.dam.agroapp.error.exception.EntityNotFoundException;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public Page<Maquina> findAll(Pageable pageable) {
        return maquinaRepository.findAll(pageable);
    }

    public Maquina findById(Long id) {
        return maquinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("máquina", id));
    }

    public Maquina create(CreateMaquinaRequest cmd) {
        if (maquinaRepository.existsByNumSerie(cmd.numeroSerie())) {
            throw new DuplicateResourceException("número de serie", cmd.numeroSerie());
        }
        Maquina maquina = Maquina.builder()
                .nombre(cmd.nombre())
                .modelo(cmd.modelo())
                .numSerie(cmd.numeroSerie())
                .fechaCompra(cmd.fechaCompra())
                .estado(EstadoMaquina.ACTIVA)
                .build();
        return maquinaRepository.save(maquina);
    }

    public MaquinaStatsDto getStats() {
        long total = maquinaRepository.count();
        long activas = maquinaRepository.countByEstado(EstadoMaquina.ACTIVA);
        long mantenimiento = maquinaRepository.countByEstado(EstadoMaquina.MANTENIMIENTO);
        return new MaquinaStatsDto(total, activas, mantenimiento);
    }

    public Maquina update(Long id, UpdateMaquinaDto cmd) {
        Maquina maquina = findById(id);
        maquina.setNombre(cmd.nombre());
        maquina.setModelo(cmd.modelo());
        maquina.setFechaCompra(cmd.fechaCompra());
        return maquinaRepository.save(maquina);
    }

    public Maquina cambiarEstado(Long id, EstadoMaquina nuevoEstado) {
        Maquina maquina = findById(id);
        maquina.setEstado(nuevoEstado);
        return maquinaRepository.save(maquina);
    }

    public void delete(Long id) {
        Maquina maquina = findById(id);
        maquinaRepository.delete(maquina);
    }
}
