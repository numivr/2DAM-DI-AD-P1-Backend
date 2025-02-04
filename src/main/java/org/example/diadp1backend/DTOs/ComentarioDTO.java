package org.example.diadp1backend.DTOs;

import lombok.Data;

@Data
public class ComentarioDTO {
  private String  UsuarioCreador;
  private String texto;
  private java.sql.Timestamp fecha;
}
