package org.example.diadp1backend.modelos;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cualidades", schema = "bbdd_santuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Cualidad {

  @Id
  @Column(name = "id_usuario")
  private Integer idUsuario;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id_usuario")
  @JsonBackReference
  private Usuario usuario;

  @Column(name = "foto")
  private String foto;

  @Column(name = "genero", nullable = false)
  private String genero;

  @Column(name = "edad", nullable = false)
  private String edad;

  @Column(name = "raza", nullable = false)
  private String raza;

  @Column(name = "nivel_energia", nullable = false)
  private Integer nivelEnergia;

  @Column(name = "sociabilidad", nullable = false)
  private Integer sociabilidad;

  @Column(name = "tamaño", nullable = false)
  private Integer tamaño;

  @Column(name = "tolerancia_especies", nullable = false)
  private Integer toleranciaEspecies;

  @Column(name = "nivel_dominancia", nullable = false)
  private Integer nivelDominancia;

  @Column(name = "tendencia_juego", nullable = false)
  private Integer tendenciaJuego;

  @Column(name = "temperamento", nullable = false)
  private Integer temperamento;

  @Column(name = "experiencia", nullable = false)
  private Integer experiencia;

  @Column(name = "territorialidad", nullable = false)
  private Integer territorialidad;
}


