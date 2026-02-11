package com.salesianostriana.dam.agroapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesianostriana.dam.agroapp.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trabajadores")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Trabajador implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String dni;

    @Column(unique = true)
    private String telefono;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDate fechaAlta;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;


    @OneToMany(mappedBy = "trabajador")
    @JsonIgnore
    @Builder.Default
    private List<Incidencia> incidencias = new ArrayList<>();

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Asignacion> asignaciones = new ArrayList<>();


    public void addIncidencia(Incidencia incidencia) {
        incidencias.add(incidencia);
        incidencia.setTrabajador(this);
    }

    public void removeIncidencia(Incidencia incidencia) {
        incidencias.remove(incidencia);
        incidencia.setTrabajador(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return this.enabled; }
}
