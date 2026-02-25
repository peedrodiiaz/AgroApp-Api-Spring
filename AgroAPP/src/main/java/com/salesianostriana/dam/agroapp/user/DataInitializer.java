package com.salesianostriana.dam.agroapp.user;

import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crea admin solo si no existe
        if (trabajadorRepository.findByEmail("admin@agroapp.com").isEmpty()) {
            Trabajador admin = Trabajador.builder()
                    .nombre("Administrador")
                    .apellido("Sistema")
                    .dni("00000000A")
                    .email("admin@agroapp.com")
                    .telefono("657474853")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .fechaAlta(LocalDate.now())
                    .enabled(true)
                    .build();

            trabajadorRepository.save(admin);

        }
    }
}
