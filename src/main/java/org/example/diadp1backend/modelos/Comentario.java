package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comentarios", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Comentario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_publicacion", nullable = false)
  private Publicacion publicacion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_usuario", nullable = false)
  private Usuario usuario;

  @Column(name = "texto", nullable = false)
  private String texto;

  @Column(name = "fecha_creacion", nullable = false)
  private java.sql.Timestamp fecha;

}
