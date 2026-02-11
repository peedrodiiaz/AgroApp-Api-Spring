package com.salesianostriana.dam.agroapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "asignaciones")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", foreignKey = @ForeignKey(name = "fk_asignacion_trabajador"))
    private Trabajador trabajador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_asignacion_maquina"))
    private Maquina maquina;
    public boolean seSolapaCon(Asignacion otra) {
        return this.fechaInicio.isBefore(otra.getFechaFin()) &&
                otra.getFechaInicio().isBefore(this.fechaFin);
    }
}
