package org.example.diadp1backend.modelos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Table(name = "usuarios", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @EqualsAndHashCode.Include // ✅ Solo el ID se usará para comparar usuarios
  private Integer id;

  @Column(name = "email", nullable = false, unique = true)
  @EqualsAndHashCode.Include // ✅ Incluir email en equals() y hashCode()
  private String email;

  @Column(name = "nombre", nullable = false, unique = true)
  @EqualsAndHashCode.Include // ✅ Incluir nombre en equals() y hashCode()
  private String nombre;

  @Column(name = "contraseña", nullable = false)
  private String contraseña;

  @Column(name = "es_admin", nullable = false)
  private Boolean esAdmin;

  @Column(name = "verificado", nullable = false)
  private Boolean verificado = false; // 🔹 Nuevo campo, por defecto false

  @Column(name = "baneado", nullable = false)
  private Boolean baneado = false; // 🔹 Nuevo campo, por defecto false

  @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Cualidad cualidad;

  @ManyToMany
  @JoinTable(
    name = "follows",
    schema = "bbdd_santuario",
    joinColumns = @JoinColumn(name = "id_seguidor"),
    inverseJoinColumns = @JoinColumn(name = "id_seguido")
  )
  private Set<Usuario> seguidos = new HashSet<>();

  @ManyToMany(mappedBy = "seguidos")
  private Set<Usuario> seguidores = new HashSet<>();

  @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<Publicacion> publicaciones = new HashSet<>();

  @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<Mensaje> mensajes = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "miembros_chat",
    schema = "bbdd_santuario",
    joinColumns = @JoinColumn(name = "id_usuario"),
    inverseJoinColumns = @JoinColumn(name = "id_chat")
  )
  private Set<Chat> chats = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(this.esAdmin.toString()));
  }

  @Override
  public String getPassword() {
    return this.contraseña;
  }

  @Override
  public String getUsername() {
    return this.nombre;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !this.baneado; // 🔹 La cuenta está bloqueada si está baneado
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !this.baneado; // 🔹 El usuario solo está habilitado si no está baneado
  }
}



