package com.salesianostriana.dam.agroapp.repository;

import com.salesianostriana.dam.agroapp.model.Trabajador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrabajadorRepository extends JpaRepository<Trabajador,Long> {

    Optional<Trabajador> findByDni(String dni);
    Optional<Trabajador> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(@Email @NotBlank String email);
    boolean existsByTelefono(String telefono);
}
