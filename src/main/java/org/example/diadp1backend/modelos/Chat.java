package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats", schema = "bbdd_santuario")
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

  public Integer getId() {
    return id;
  }

  public Set<Mensaje> getMensajes() {
    return mensajes;
  }

  public Set<Usuario> getMiembros() {
    return miembros;
  }

  public String getNombre() {
    return nombre;
  }

  public String getTipo() {
    return tipo;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setMensajes(Set<Mensaje> mensajes) {
    this.mensajes = mensajes;
  }

  public void setMiembros(Set<Usuario> miembros) {
    this.miembros = miembros;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
}
