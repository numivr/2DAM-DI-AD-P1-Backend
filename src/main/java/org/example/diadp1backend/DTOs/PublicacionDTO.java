package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO {
  private Integer id;
  private Integer idCreador; // ✅ Nuevo campo para almacenar el ID del creador
  private String perfil;
  private String fotoPerfil;
  private String texto;
  private String fotoPublicacion;
  private int numComentarios;
  private int numLikes;
  private boolean liked;
  private ArrayList<ComentarioDTO> comentarios;

  public PublicacionDTO(Integer id, Integer idCreador, String texto, String imagen) {
    this.id = id;
    this.idCreador = idCreador;
    this.texto = texto;
    this.fotoPublicacion = imagen;
  }
}
