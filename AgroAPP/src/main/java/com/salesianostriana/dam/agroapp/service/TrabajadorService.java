package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.dto.trabajador.CreateTrabajadorRequest;
import com.salesianostriana.dam.agroapp.dto.trabajador.UpdateTrabajadorRequest;
 import com.salesianostriana.dam.agroapp.error.exception.DuplicateResourceException;
import com.salesianostriana.dam.agroapp.error.exception.EntityNotFoundException;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import com.salesianostriana.dam.agroapp.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public Page <Trabajador>getAll(Pageable pageable) {
        return trabajadorRepository.findAll(pageable);
    }

    public Trabajador create(CreateTrabajadorRequest request) {

        
        if (trabajadorRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("email", request.email());
        }
        if (trabajadorRepository.existsByDni(request.dni())) {
            throw new DuplicateResourceException("DNI", request.dni());
        }
        if (trabajadorRepository.existsByTelefono(request.telefono())) {
            throw new DuplicateResourceException("teléfono", request.telefono());
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

        Trabajador guardado = trabajadorRepository.save(trabajador);
        return guardado;
    }

    public Trabajador findByEmail(String email) {
        return trabajadorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se ha encontrado ningún trabajador con email: " + email));
    }

    public Trabajador update(Long id, UpdateTrabajadorRequest request) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("trabajador", id));

        if (request.email() != null && !request.email().equals(trabajador.getEmail())) {
            if (trabajadorRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException("email", request.email());
            }
            trabajador.setEmail(request.email());
        }

        if (request.telefono() != null && !request.telefono().equals(trabajador.getTelefono())) {
            if (trabajadorRepository.existsByTelefono(request.telefono())) {
                throw new DuplicateResourceException("teléfono", request.telefono());
            }
            trabajador.setTelefono(request.telefono());
        }

        if (request.nombre() != null) trabajador.setNombre(request.nombre());
        if (request.apellido() != null) trabajador.setApellido(request.apellido());

        return trabajadorRepository.save(trabajador);
    }
    public void delete(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("trabajador", id));
        trabajador.setEnabled(false);
        trabajadorRepository.save(trabajador);
    }

    public Trabajador cambiarActivoInactivo(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("trabajador", id));
        trabajador.setEnabled(!trabajador.isEnabled());
        return trabajadorRepository.save(trabajador);
    }

    public Trabajador updateFoto(Long id, MultipartFile file) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("trabajador", id));
        if (trabajador.getFotoPerfil() != null) {
            storageService.deleteFile(trabajador.getFotoPerfil());
        }
        String filename = storageService.store(file);
        trabajador.setFotoPerfil(filename);
        return trabajadorRepository.save(trabajador);
    }

}
