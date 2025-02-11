package org.example.diadp1backend.DTOs;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {

  private String usuario;
  private String password;
  private String email;

  // 📌 Cualidades agregadas al DTO
  private String foto;
  private String genero;
  private String edad;
  private String raza;
  private Integer nivelEnergia;
  private Integer sociabilidad;
  private Integer tamaño;
  private Integer toleranciaEspecies;
  private Integer nivelDominancia;
  private Integer tendenciaJuego;
  private Integer temperamento;
  private Integer experiencia;
  private Integer territorialidad;
}
