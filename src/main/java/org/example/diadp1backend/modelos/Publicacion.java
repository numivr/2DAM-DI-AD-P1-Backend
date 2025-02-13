package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "publicaciones", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Publicacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "texto")
  private String texto;

  @Column(name = "imagen")
  private String imagen;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_creador", nullable = false)
  private Usuario creador;

  @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<Comentario> comentarios = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "likes",
    schema = "bbdd_santuario",
    joinColumns = @JoinColumn(name = "id_publicacion"),
    inverseJoinColumns = @JoinColumn(name = "id_usuario")
  )
  private Set<Usuario> likes = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(id); // ⚠️ Solo usa el ID, NO la colección de comentarios
  }


}
