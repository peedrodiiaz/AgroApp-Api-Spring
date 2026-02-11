package com.salesianostriana.dam.agroapp.model;

import com.salesianostriana.dam.agroapp.enums.EstadoIncidencia;
import com.salesianostriana.dam.agroapp.enums.Prioridad;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "incidencia_id_gen", table = "id_sequences", pkColumnName = "sequence_name", valueColumnName = "next_val", initialValue = 1, allocationSize = 1)
    private Long id;

    private String titulo;

    @Lob
    private String descripcion;

    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;

    @Enumerated (EnumType.STRING)
    private Prioridad prioridad;

    @Enumerated (EnumType.STRING)
    private EstadoIncidencia estadoIncidencia;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", foreignKey = @ForeignKey (name = "fk_incidencia_trabajador"))
    private Trabajador trabajador;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey (name = "fk_incidencia_maquina"))
    private  Maquina maquina;



}
