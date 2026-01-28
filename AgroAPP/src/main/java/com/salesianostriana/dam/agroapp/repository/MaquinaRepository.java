package com.salesianostriana.dam.agroapp.repository;

import com.salesianostriana.dam.agroapp.model.EstadoMaquina;
import com.salesianostriana.dam.agroapp.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina,Long> {

    Optional<Maquina> findByNumSerie(String numSerie);
    List<Maquina> findByEstado(EstadoMaquina estado);
    List<Maquina> findByModeloContainingIgnoreCase(String modelo);
}
