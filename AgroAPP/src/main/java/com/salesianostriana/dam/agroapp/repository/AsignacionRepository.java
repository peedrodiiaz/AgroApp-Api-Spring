package com.salesianostriana.dam.agroapp.repository;

import com.salesianostriana.dam.agroapp.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByTrabajadorId(Long trabajadorId);

    List<Asignacion> findByMaquinaId(Long maquinaId);

    @Query("SELECT COUNT(a) > 0 FROM Asignacion a WHERE " +
            "a.maquina.id = :maquinaId AND " +
            "a.fechaInicio <= :fechaFin AND " +
            "a.fechaFin >= :fechaInicio")
    boolean existsSolapamiento(
            @Param("maquinaId") Long maquinaId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("SELECT COUNT(a) > 0 FROM Asignacion a WHERE " +
            "a.maquina.id = :maquinaId AND " +
            "a.id <> :asignacionId AND " +
            "a.fechaInicio <= :fechaFin AND " +
            "a.fechaFin >= :fechaInicio")
    boolean existsSolapamientoExceptoId(
            @Param("maquinaId") Long maquinaId,
            @Param("asignacionId") Long asignacionId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("SELECT a FROM Asignacion a WHERE " +
            "a.fechaInicio <= :hoy AND a.fechaFin >= :hoy")
    List<Asignacion> findAsignacionesActivas(@Param("hoy") LocalDate hoy);
}
