package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mensajes", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Mensaje {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_chat", nullable = false)
  private Chat chat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_emisor", nullable = false)
  private Usuario emisor;

  @Column(name = "contenido", nullable = false)
  private String contenido;

  @Column(name = "fecha", nullable = false)
  private java.sql.Timestamp fecha;

}
