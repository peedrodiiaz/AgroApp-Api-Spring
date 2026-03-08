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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    @Value("${storage.location:./uploads}")
    private String storageLocation;

    private String downloadImage(String urlStr, String filename) {
        try {
            Path uploadsDir = Path.of(storageLocation);
            Path targetPath = uploadsDir.resolve(filename);
            if (Files.exists(targetPath)) return filename;
            Files.createDirectories(uploadsDir);
            URI uri = URI.create(urlStr);
            try (InputStream in = uri.toURL().openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void run(String... args) {

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
                    .fotoPerfil(downloadImage("https://randomuser.me/api/portraits/men/1.jpg", "foto-admin.jpg"))
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
                    .fotoPerfil(downloadImage("https://randomuser.me/api/portraits/men/42.jpg", "foto-pedro.jpg"))
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
                    .fotoPerfil(downloadImage("https://randomuser.me/api/portraits/women/23.jpg", "foto-maria.jpg"))
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
                    .fotoPerfil(downloadImage("https://randomuser.me/api/portraits/men/77.jpg", "foto-carlos.jpg"))
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
                    .fotoPerfil(downloadImage("https://randomuser.me/api/portraits/women/55.jpg", "foto-laura.jpg"))
                    .build();
            trabajadorRepository.save(laura);
        }

        if (maquinaRepository.count() == 0) {

            List<Maquina> maquinas = List.of(
                Maquina.builder()
                    .nombre("Tractor John Deere")
                    .modelo("John Deere 6130R")
                    .numSerie("JD6130R-001")
                    .fechaCompra(LocalDate.of(2020, 3, 15))
                    .estado(EstadoMaquina.ACTIVA)
                    .imagen(downloadImage("https://www.lectura-specs.es/models/renamed/orig/tractores---traccion-4-ruedas-6130r-john-deere.jpg", "maquina-tractor-jd.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Cosechadora New Holland")
                    .modelo("New Holland CR9.90")
                    .numSerie("NH-CR990-002")
                    .fechaCompra(LocalDate.of(2019, 7, 22))
                    .estado(EstadoMaquina.ACTIVA)
                    .imagen(downloadImage("https://m.media-amazon.com/images/I/71IjnqYMV6L._AC_UF894,1000_QL80_.jpg", "maquina-cosechadora-nh.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Pulverizadora HARDI")
                    .modelo("HARDI COMMANDER 4200")
                    .numSerie("HARDI-4200-003")
                    .fechaCompra(LocalDate.of(2021, 1, 10))
                    .estado(EstadoMaquina.MANTENIMIENTO)
                    .imagen(downloadImage("https://ik.imagekit.io/efarm/images/7c59bbeb-3eb9-4004-a8a0-4e1f86b46789.jpg?tr=w-600%2Car-1000-667%2Cl-image%2Ci-%40%40website-machine-images-watermarks%40%40watermark_DZDDMXPYs_M9F2mQxfH.png%2Clx-6%2Cly-6%2Cw-90%2Cl-end", "maquina-pulverizadora.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Sembradora Amazone")
                    .modelo("Amazone D9 4000")
                    .numSerie("AMZ-D9-004")
                    .fechaCompra(LocalDate.of(2022, 6, 30))
                    .estado(EstadoMaquina.ACTIVA)
                    .imagen(downloadImage("https://www.mazas.es/archivos/image/catalogo_productos/medias/55-229-productosembradoras-d9ad.jpg", "maquina-sembradora.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Remolque Basculante")
                    .modelo("Joskin Trans-Space 6600")
                    .numSerie("JOS-6600-005")
                    .fechaCompra(LocalDate.of(2018, 11, 5))
                    .estado(EstadoMaquina.ACTIVA)
                    .imagen(downloadImage("https://quote.joskin.com/system/products/headers/000/000/055/original/0403_Trans-SPACE-202411-1092x580-min.jpg", "maquina-remolque.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Cultivador Lemken")
                    .modelo("Lemken Heliodor 9")
                    .numSerie("LEM-HEL9-006")
                    .fechaCompra(LocalDate.of(2023, 2, 14))
                    .estado(EstadoMaquina.ACTIVA)
                    .imagen(downloadImage("https://dnge9sb91helb.cloudfront.net/imagetilewm/product/jma/lemken-heliodor-9-70,333bb0ba_8720026_1.jpg", "maquina-cultivador.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Pala Cargadora Claas")
                    .modelo("Claas Torion 639")
                    .numSerie("CLS-TOR639-007")
                    .fechaCompra(LocalDate.of(2017, 9, 19))
                    .estado(EstadoMaquina.INACTIVA)
                    .imagen(downloadImage("https://cdn.truckscout24.com/data/listing/img/vga/ts/90/38/20866832-01.jpg?v=1767656852", "maquina-pala.jpg"))
                    .build(),
                Maquina.builder()
                    .nombre("Empacadora Krone")
                    .modelo("Krone BiG Pack 1290")
                    .numSerie("KRN-BP1290-008")
                    .fechaCompra(LocalDate.of(2021, 4, 28))
                    .estado(EstadoMaquina.MANTENIMIENTO)
                    .imagen(downloadImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdC1zpFe-40MSP7Pm569uKDeygqAosAGw-7A&s", "maquina-empacadora.jpg"))
                    .build()
            );

            maquinaRepository.saveAll(maquinas);

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

            Maquina pulverizadora = maquinas.get(2);

            List<Incidencia> incidencias = List.of(
                Incidencia.builder()
                    .titulo("Fallo en sistema hidraulico")
                    .descripcion("El tractor presenta perdida de presion en el circuito hidraulico trasero. Se detecta fuga en manguera de retorno.")
                    .fechaApertura(LocalDateTime.of(2025, 2, 10, 8, 30))
                    .fechaCierre(LocalDateTime.of(2025, 2, 12, 16, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .latitud(38.9177).longitud(-6.3424)
                    .trabajador(pedro).maquina(tractor)
                    .build(),
                Incidencia.builder()
                    .titulo("Correa de transmision desgastada")
                    .descripcion("La cosechadora presenta ruido anormal durante la trilla. Se diagnostica desgaste excesivo en correa principal de transmision.")
                    .fechaApertura(LocalDateTime.of(2025, 7, 10, 10, 0))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.EN_PROGRESO)
                    .latitud(37.3891).longitud(-5.9845)
                    .trabajador(maria).maquina(cosechadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Boquillas de pulverizacion obstruidas")
                    .descripcion("Varias boquillas del circuito de pulverizacion presentan obstruccion parcial. Tratamiento desigual en parcelas.")
                    .fechaApertura(LocalDateTime.of(2025, 5, 3, 9, 15))
                    .fechaCierre(LocalDateTime.of(2025, 5, 4, 12, 0))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .latitud(36.6819).longitud(-6.1367)
                    .trabajador(carlos).maquina(pulverizadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Sembradora sin calibrar")
                    .descripcion("La sembradora no distribuye correctamente la dosis de semilla. Requiere recalibrado del dosificador.")
                    .fechaApertura(LocalDateTime.of(2025, 3, 2, 7, 0))
                    .fechaCierre(LocalDateTime.of(2025, 3, 3, 14, 30))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .latitud(38.8794).longitud(-6.9706)
                    .trabajador(carlos).maquina(sembradora)
                    .build(),
                Incidencia.builder()
                    .titulo("Neumatico pinchado en remolque")
                    .descripcion("El remolque presenta pinchazo en rueda trasera derecha durante transporte en camino de tierra.")
                    .fechaApertura(LocalDateTime.of(2025, 2, 15, 11, 45))
                    .fechaCierre(LocalDateTime.of(2025, 2, 15, 15, 0))
                    .prioridad(Prioridad.BAJA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .latitud(37.8882).longitud(-4.7794)
                    .trabajador(pedro).maquina(remolque)
                    .build(),
                Incidencia.builder()
                    .titulo("Motor de empacadora averiado")
                    .descripcion("La empacadora no arranca. Se detecta averia en el motor electrico de accionamiento de la camara de compresion.")
                    .fechaApertura(LocalDateTime.of(2025, 6, 12, 8, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.EN_PROGRESO)
                    .latitud(40.9701).longitud(-5.6635)
                    .trabajador(laura).maquina(empacadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Desgaste de discos del cultivador")
                    .descripcion("Los discos del cultivador presentan desgaste superior al 40%. Necesitan sustitucion antes de la proxima campana.")
                    .fechaApertura(LocalDateTime.of(2025, 4, 16, 9, 30))
                    .prioridad(Prioridad.BAJA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .latitud(39.8628).longitud(-4.0273)
                    .trabajador(laura).maquina(cultivador)
                    .build(),
                Incidencia.builder()
                    .titulo("Averia en sistema de GPS del tractor")
                    .descripcion("El sistema de guiado GPS del tractor no establece senal correcta. Afecta a la precision en labores de siembra directa.")
                    .fechaApertura(LocalDateTime.of(2025, 9, 5, 7, 30))
                    .prioridad(Prioridad.MEDIA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .latitud(38.9942).longitud(-1.8564)
                    .trabajador(maria).maquina(tractor)
                    .build(),
                Incidencia.builder()
                    .titulo("Rotura de cadena en cosechadora")
                    .descripcion("La cosechadora ha sufrido rotura de cadena de elevador de grano. Parada total de operaciones.")
                    .fechaApertura(LocalDateTime.of(2025, 7, 20, 14, 0))
                    .fechaCierre(LocalDateTime.of(2025, 7, 22, 10, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.RESUELTA)
                    .latitud(41.6488).longitud(-0.8891)
                    .trabajador(maria).maquina(cosechadora)
                    .build(),
                Incidencia.builder()
                    .titulo("Fugas de aceite en diferencial")
                    .descripcion("El tractor presenta perdida de aceite en el diferencial trasero. Nivel por debajo del minimo recomendado.")
                    .fechaApertura(LocalDateTime.of(2025, 10, 1, 8, 0))
                    .prioridad(Prioridad.ALTA)
                    .estadoIncidencia(EstadoIncidencia.ABIERTA)
                    .latitud(41.6523).longitud(-4.7245)
                    .trabajador(pedro).maquina(tractor)
                    .build()
            );

            incidenciaRepository.saveAll(incidencias);
        }
    }
}
