package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "tipo", nullable = false)
  private String tipo;

  @ManyToMany(mappedBy = "chats")
  private Set<Usuario> miembros = new HashSet<>();

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Mensaje> mensajes = new HashSet<>();


}
