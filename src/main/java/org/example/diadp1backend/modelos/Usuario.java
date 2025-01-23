package org.example.diadp1backend.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "usuarios", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "nombre", nullable = false, unique = true)
  private String nombre;

  @Column(name = "contraseña", nullable = false)
  private String contraseña;

  @Column(name = "es_admin", nullable = true)
  private Boolean esAdmin;

  @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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

  @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Publicacion> publicaciones = new HashSet<>();

  @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Mensaje> mensajes = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "miembros_chat",
    schema = "bbdd_santuario",
    joinColumns = @JoinColumn(name = "id_usuario"),
    inverseJoinColumns = @JoinColumn(name = "id_chat")
  )
  private Set<Chat> chats = new HashSet<>();
}


