package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO {
  private String perfil; // Nombre del creador de la publicación
  private String fotoPerfil; // Foto del perfil del creador
  private String texto; // Texto de la publicación
  private String fotoPublicacion; // Imagen asociada a la publicación
  private String numComentarios; // Número de comentarios en la publicación
  private int numLikes; // Número de 'Me gusta' en la publicación
  private boolean liked; // Si el usuario actual ha dado 'Me gusta' o no
}
