package com.salesianostriana.dam.agroapp.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  private String nombrel;
  private String modelo;
  private String numSerie;

  private LocalDate fechaCompra;

  private EstadoMaquina estadoMaquina;


}
