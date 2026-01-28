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
public class Trabajador {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String apellido;

  @Column(unique = true)
  private String dni;
  
  @Column (unique = true)
  private int telefono;
  
  private String email;
  private LocalDate fechaAlta;



  @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Incidencia> incidencias = new ArrayList<>();

  @OneToMany (mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List <Asignacion>asignacions = new ArrayList<>();


    public void addIncidencia(Incidencia incidencia) {
        incidencias.add(incidencia);
        incidencia.setTrabajador(this);
    }

    public void removeIncidencia(Incidencia incidencia) {
        incidencias.remove(incidencia);
        incidencia.setTrabajador(null);
    }
}
