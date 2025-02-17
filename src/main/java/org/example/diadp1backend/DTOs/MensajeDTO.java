package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MensajeDTO {
  private Integer id;
  private Integer idChat;
  private Integer idEmisor;
  private String nombreEmisor;
  private String contenido;
  private java.sql.Timestamp fecha;
}
