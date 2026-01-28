package com.salesianostriana.dam.agroapp.repository;

import com.salesianostriana.dam.agroapp.model.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia,Long> {

    List<Incidencia> findByTrabajadorId(Long id);
    List<Incidencia> findByMaquinaId(Long id);
    List<Incidencia> findByEstado(EstadoIncidencia estado);




}
