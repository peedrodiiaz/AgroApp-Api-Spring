package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.trabajador.CreateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.UpdateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder; 

    public Page <Trabajador>getAll(Pageable pageable) {
        return trabajadorRepository.findByEnabledTrue(pageable);
    }


    public Trabajador create(CreateTrabajadorRequest request) {

        
        if (trabajadorRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (trabajadorRepository.existsByDni(request.dni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (trabajadorRepository.existsByTelefono(request.telefono())) {
            throw new RuntimeException("El teléfono ya está registrado");
        }

        Trabajador trabajador = Trabajador.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .dni(request.dni())
                .email(request.email())
                .telefono(request.telefono())
                .fechaAlta(LocalDate.now())
                .rol(request.rol())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .build();

        Trabajador guardado = trabajadorRepository.save(trabajador);return guardado;
    }

    public Trabajador findByEmail(String email) {
        return trabajadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Trabajador update(Long id, UpdateTrabajadorRequest request) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        if (request.email() != null && !request.email().equals(trabajador.getEmail())) {
            if (trabajadorRepository.existsByEmail(request.email())) {
                throw new RuntimeException("El email ya está en uso");
            }
            trabajador.setEmail(request.email());
        }

        if (request.telefono() != null && !request.telefono().equals(trabajador.getTelefono())) {
            if (trabajadorRepository.existsByTelefono(request.telefono())) {
                throw new RuntimeException("El teléfono ya está en uso");
            }
            trabajador.setTelefono(request.telefono());
        }

        if (request.nombre() != null) trabajador.setNombre(request.nombre());
        if (request.apellido() != null) trabajador.setApellido(request.apellido());

        return trabajadorRepository.save(trabajador);
    }
    public void delete(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        trabajador.setEnabled(false);
        trabajadorRepository.save(trabajador);
    }

    public Trabajador cambiarActivoInactivo(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        trabajador.setEnabled(!trabajador.isEnabled());

        return trabajadorRepository.save(trabajador);
    }

}
