package org.example.diadp1backend.DTOs;

import java.util.List;

public class PublicacionDTO {
  private Integer idPublicacion; //Esto se usará para listar los comentarios
  private String usuarioCreador; //Esto solo se usa en el get de publicacion
  private String texto;
  private String imagen;
  private Integer idCreador; //Esto solo se usa en el post de publicacion
  private Integer numLikes; //Esto se usara para saber cuantos likes tiene una publicacion directamente al traernos sus datos
}
