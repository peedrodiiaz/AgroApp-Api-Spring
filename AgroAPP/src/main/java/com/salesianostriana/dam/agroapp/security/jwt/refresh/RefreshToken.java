package com.salesianostriana.dam.agroapp.security.jwt.refresh;

import com.salesianostriana.dam.agroapp.model.Trabajador;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "trabajador_id")
  private Trabajador trabajador;

  @Column(nullable = false)
  private Instant expireAt;

  @Builder.Default
  private Instant createdAt = Instant.now();

  public String getToken() {
    return id.toString();
  }
}
