package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComentarioDTO {
  private Integer id;
  private Integer idPublicacion;
  private String usuarioCreador;
  private String fotoCreador;
  private String texto;
  private String fecha;

}
