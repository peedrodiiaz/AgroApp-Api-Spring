package com.salesianostriana.dam.agroapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Maquina {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private String modelo;
  private String numSerie;

  private LocalDate fechaCompra;

  @Enumerated (EnumType.STRING)
  private EstadoMaquina estado;

  @OneToMany (mappedBy = "maquina", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Incidencia> incidencias=new ArrayList<>();

    @OneToMany (mappedBy = "maquina", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
  private List <Asignacion>asignaciones = new ArrayList<>();

  public void addIncidencia(Incidencia incidencia) {
        incidencias.add(incidencia);
        incidencia.setMaquina(this);


  }


}
