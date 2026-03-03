package com.salesianostriana.dam.agroapp.user;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.EstadoMaquina;
import com.salesianostriana.dam.agroapp.enums.Prioridad;
import com.salesianostriana.dam.agroapp.enums.Rol;
import com.salesianostriana.dam.agroapp.model.Asignacion;
import com.salesianostriana.dam.agroapp.model.Incidencia;
import com.salesianostriana.dam.agroapp.model.Maquina;
import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.AsignacionRepository;
import com.salesianostriana.dam.agroapp.repository.IncidenciaRepository;
import com.salesianostriana.dam.agroapp.repository.MaquinaRepository;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TrabajadorRepository trabajadorRepository;
    private final MaquinaRepository maquinaRepository;
    private final AsignacionRepository asignacionRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── TRABAJADORES ──────────────────────────────────────────────────────────
        if (trabajadorRepository.findByEmail("admin@agroapp.com").isEmpty()) {
            Trabajador admin = Trabajador.builder()
                    .nombre("Administrador")
                    .apellido("Sistema")
                    .dni("00000000A")
                    .email("admin@agroapp.com")
                    .telefono("657474853")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .fechaAlta(LocalDate.of(2024, 1, 15))
                    .enabled(true)
                    .build();
            trabajadorRepository.save(admin);
        }

        if (trabajadorRepository.findByEmail("pedro@agroapp.com").isEmpty()) {
            Trabajador pedro = Trabajador.builder()
                    .nombre("Pedro")
                    .apellido("Diaz")
                    .dni("77583424V")
                    .email("pedro@agroapp.com")
                    .telefono("65344853")
                    .password(passwordEncoder.encode("pedro123"))
                    .rol(Rol.TRABAJADOR)
                    .fechaAlta(LocalDate.of(2024, 2, 1))
                    .enabled(true)
                    .build();
            trabajadorRepository.save(pedro);
        }

        if (trabajadorRepository.findByEmail("maria@agroapp.com").isEmpty()) {
            Trabajador maria = Trabajador.builder()
                    .nombre("Maria")
                    .apellido("Gonzalez")
                    .dni("12345678B")
                    .email("maria@agroapp.com")
                    .telefono("611223344")
                    .password(passwordEncoder.encode("trabaj123"))
                    .rol(Rol.TRABAJADOR)
                    .fechaAlta(LocalDate.of(2024, 3, 10))
                    .enabled(true)
                    .build();
            trabajadorRepository.save(maria);
        }

        if (trabajadorRepository.findByEmail("carlos@agroapp.com").isEmpty()) {
            Trabajador carlos = Trabajador.builder()
                    .nombre("Carlos")
                    .apellido("Fernandez")
                    .dni("87654321C")
                    .email("carlos@agroapp.com")
                    .telefono("622334455")
                    .password(passwordEncoder.encode("trabaj123"))
                    .rol(Rol.TRABAJADOR)
                    .fechaAlta(LocalDate.of(2024, 4, 5))
                    .enabled(true)
                    .build();
            trabajadorRepository.save(carlos);
        }

        if (trabajadorRepository.findByEmail("laura@agroapp.com").isEmpty()) {
            Trabajador laura = Trabajador.builder()
                    .nombre("Laura")
                    .apellido("Martinez")
                    .dni("11223344D")
                    .email("laura@agroapp.com")
                    .telefono("633445566")
                    .password(passwordEncoder.encode("trabaj123"))
                    .rol(Rol.TRABAJADOR)
                    .fechaAlta(LocalDate.of(2024, 5, 20))
                    .enabled(true)
                    .build();
            trabajadorRepository.save(laura);
        }

        // ── MÁQUINAS ──────────────────────────────────────────────────────────────
        if (maquinaRepository.count() == 0) {

            List<Maquina> maquinas = List.of(
                Maquina.builder()
                    .nombre("Tractor John Deere")
                    .modelo("John Deere 6130R")
                    .numSerie("JD6130R-001")
                    .fechaCompra(LocalDate.of(2020, 3, 15))
                    .estado(EstadoMaquina.ACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Cosechadora New Holland")
                    .modelo("New Holland CR9.90")
                    .numSerie("NH-CR990-002")
                    .fechaCompra(LocalDate.of(2019, 7, 22))
                    .estado(EstadoMaquina.ACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Pulverizadora HARDI")
                    .modelo("HARDI COMMANDER 4200")
                    .numSerie("HARDI-4200-003")
                    .fechaCompra(LocalDate.of(2021, 1, 10))
                    .estado(EstadoMaquina.MANTENIMIENTO)
                    .build(),
                Maquina.builder()
                    .nombre("Sembradora Amazone")
                    .modelo("Amazone D9 4000")
                    .numSerie("AMZ-D9-004")
                    .fechaCompra(LocalDate.of(2022, 6, 30))
                    .estado(EstadoMaquina.ACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Remolque Basculante")
                    .modelo("Joskin Trans-Space 6600")
                    .numSerie("JOS-6600-005")
                    .fechaCompra(LocalDate.of(2018, 11, 5))
                    .estado(EstadoMaquina.ACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Cultivador Lemken")
                    .modelo("Lemken Heliodor 9")
                    .numSerie("LEM-HEL9-006")
                    .fechaCompra(LocalDate.of(2023, 2, 14))
                    .estado(EstadoMaquina.ACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Pala Cargadora Claas")
                    .modelo("Claas Torion 639")
                    .numSerie("CLS-TOR639-007")
                    .fechaCompra(LocalDate.of(2017, 9, 19))
                    .estado(EstadoMaquina.INACTIVA)
                    .build(),
                Maquina.builder()
                    .nombre("Empacadora Krone")
                    .modelo("Krone BiG Pack 1290")
                    .numSerie("KRN-BP1290-008")
                    .fechaCompra(LocalDate.of(2021, 4, 28))
                    .estado(EstadoMaquina.MANTENIMIENTO)
                    .build()
            );

            maquinaRepository.saveAll(maquinas);

            // ── ASIGNACIONES ──────────────────────────────────────────────────────
            Trabajador pedro  = trabajadorRepository.findByEmail("pedro@agroapp.com").orElseThrow();
            Trabajador maria  = trabajadorRepository.findByEmail("maria@agroapp.com").orElseThrow();
            Trabajador carlos = trabajadorRepository.findByEmail("carlos@agroapp.com").orElseThrow();
            Trabajador laura  = trabajadorRepository.findByEmail("laura@agroapp.com").orElseThrow();

            Maquina tractor      = maquinas.get(0);
            Maquina cosechadora  = maquinas.get(1);
            Maquina sembradora   = maquinas.get(3);
            Maquina remolque     = maquinas.get(4);
            Maquina cultivador   = maquinas.get(5);
            Maquina empacadora   = maquinas.get(7);

            List<Asignacion> asignaciones = List.of(
                Asignacion.builder()
                    .trabajador(pedro).maquina(tractor)
                    .fechaInicio(LocalDate.of(2025, 1, 10))
                    .fechaFin(LocalDate.of(2025, 1, 20))
                    .descripcion("Labores de arado en parcela norte. Preparacion del terreno para siembra de cereal.")
                    .build(),
                Asignacion.builder()
                    .trabajador(maria).maquina(cosechadora)
                    .fechaInicio(LocalDate.of(2025, 7, 5))
                    .fechaFin(LocalDate.of(2025, 7, 25))
                    .descripcion("Recoleccion de trigo en parcelas este y oeste. Campana de verano.")
                    .build(),
                Asignacion.builder()
                    .trabajador(carlos).maquina(sembradora)
                    .fechaInicio(LocalDate.of(2025, 3, 1))
                    .fechaFin(LocalDate.of(2025, 3, 5))
                    .descripcion("Siembra de girasol en finca La Esperanza. Control de profundidad de siembra.")
                    .build(),
                Asignacion.builder()
                    .trabajador(pedro).maquina(remolque)
                    .fechaInicio(LocalDate.of(2025, 2, 14))
                    .fechaFin(LocalDate.of(2025, 2, 16))
                    .descripcion("Transporte de abono organico desde almacen a parcelas sur.")
                    .build(),
                Asignacion.builder()
                    .trabajador(laura).maquina(cultivador)
                    .fechaInicio(LocalDate.of(2025, 4, 10))
                    .fechaFin(LocalDate.of(2025, 4, 15))
                    .descripcion("Trabajo de cultivador para eliminacion de malas hierbas en cereal de invierno.")
                    .build(),
                Asignacion.builder()
                    .trabajador(maria).maquina(tractor)
                    .fechaInicio(LocalDate.of(2025, 9, 1))
                    .fechaFin(LocalDate.of(2025, 9, 12))
                    .descripcion("Labores de subsolado en parcela nueva incorporada. Preparacion post-cosecha.")
                    .build(),
                Asignacion.builder()
                    .trabajador(carlos).maquina(empacadora)
                    .fechaInicio(LocalDate.of(2025, 6, 15))
                    .fechaFin(LocalDate.of(2025, 6, 20))
                    .descripcion("Empacado de heno de alfalfa para almacenamiento. Produccion de pacas grandes.")
                    .build(),
                Asignacion.builder()
                    .trabajador(laura).maquina(sembradora)
                    .fechaInicio(LocalDate.of(2025, 11, 3))
                    .fechaFin(LocalDate.of(2025, 11, 8))
                    .descripcion("Siembra de trigo blando de ciclo largo para la proxima campana.")
                    .build()
            );

            asignacionRepository.saveAll(asignaciones);

            // ── INCIDENCIAS ───────────────────────────────────────────────────────
            Maquina pulverizadora = maquinas.get(2);

            List<Incidencia> incidencias = List.of(
                Incidencia.builder()
                    .titulo("Fallo en sistema hidraulico")
                    .descripcion("El tractor presenta perdida de presion en el circuito hidraulico trasero. Se detecta fuga en manguera de retorno.")
                    .fechaApertura(LocalDateTime.of(2025, 2, 10, 8, 30))
                    .fechaCierre(LocalDateTime.of(2025, 2, 12, 16, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .trabajador(pedro).maquina(tractor)
                    .build(),
                Incidencia.builder()
                    .titulo("Correa de transmision desgastada")
                    .descripcion("La cosechadora presenta ruido anormal durante la trilla. Se diagnostica desgaste excesivo en correa principal de transmision.")
                    .fechaApertura(LocalDateTime.of(2025, 7, 10, 10, 0))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.EN_PROGRESO)
                    .trabajador(maria).maquina(cosechadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Boquillas de pulverizacion obstruidas")
                    .descripcion("Varias boquillas del circuito de pulverizacion presentan obstruccion parcial. Tratamiento desigual en parcelas.")
                    .fechaApertura(LocalDateTime.of(2025, 5, 3, 9, 15))
                    .fechaCierre(LocalDateTime.of(2025, 5, 4, 12, 0))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .trabajador(carlos).maquina(pulverizadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Sembradora sin calibrar")
                    .descripcion("La sembradora no distribuye correctamente la dosis de semilla. Requiere recalibrado del dosificador.")
                    .fechaApertura(LocalDateTime.of(2025, 3, 2, 7, 0))
                    .fechaCierre(LocalDateTime.of(2025, 3, 3, 14, 30))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .trabajador(carlos).maquina(sembradora)
                    .build(),
                Incidencia.builder()
                    .titulo("Neumatico pinchado en remolque")
                    .descripcion("El remolque presenta pinchazo en rueda trasera derecha durante transporte en camino de tierra.")
                    .fechaApertura(LocalDateTime.of(2025, 2, 15, 11, 45))
                    .fechaCierre(LocalDateTime.of(2025, 2, 15, 15, 0))
                    .prioridad(Prioridad.BAJA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .trabajador(pedro).maquina(remolque)
                    .build(),
                Incidencia.builder()
                    .titulo("Motor de empacadora averiado")
                    .descripcion("La empacadora no arranca. Se detecta averia en el motor electrico de accionamiento de la camara de compresion.")
                    .fechaApertura(LocalDateTime.of(2025, 6, 12, 8, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.EN_PROGRESO)
                    .trabajador(laura).maquina(empacadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Desgaste de discos del cultivador")
                    .descripcion("Los discos del cultivador presentan desgaste superior al 40%. Necesitan sustitucion antes de la proxima campana.")
                    .fechaApertura(LocalDateTime.of(2025, 4, 16, 9, 30))
                    .prioridad(Prioridad.BAJA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .trabajador(laura).maquina(cultivador)
                    .build(),
                Incidencia.builder()
                    .titulo("Averia en sistema de GPS del tractor")
                    .descripcion("El sistema de guiado GPS del tractor no establece senal correcta. Afecta a la precision en labores de siembra directa.")
                    .fechaApertura(LocalDateTime.of(2025, 9, 5, 7, 30))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .trabajador(maria).maquina(tractor)
                    .build(),
                Incidencia.builder()
                    .titulo("Rotura de cadena en cosechadora")
                    .descripcion("La cosechadora ha sufrido rotura de cadena de elevador de grano. Parada total de operaciones.")
                    .fechaApertura(LocalDateTime.of(2025, 7, 20, 14, 0))
                    .fechaCierre(LocalDateTime.of(2025, 7, 22, 10, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .trabajador(maria).maquina(cosechadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Fugas de aceite en diferencial")
                    .descripcion("El tractor presenta perdida de aceite en el diferencial trasero. Nivel por debajo del minimo recomendado.")
                    .fechaApertura(LocalDateTime.of(2025, 10, 1, 8, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .trabajador(pedro).maquina(tractor)
                    .build()
            );

            incidenciaRepository.saveAll(incidencias);
        }
    }
}
