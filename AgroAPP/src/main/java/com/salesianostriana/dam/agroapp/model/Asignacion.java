package com.salesianostriana.dam.agroapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "maquina_id")
    private Maquina maquina;
    public boolean seSolapaCon(Asignacion otra) {
        return this.fechaInicio.isBefore(otra.getFechaFin()) &&
                otra.getFechaInicio().isBefore(this.fechaFin);
    }
}
