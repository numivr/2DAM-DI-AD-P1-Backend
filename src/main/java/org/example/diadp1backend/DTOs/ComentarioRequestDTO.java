package org.example.diadp1backend.DTOs;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ComentarioRequestDTO {
  @NotNull(message = "El ID de la publicación no puede ser nulo")
  private Integer idPublicacion;

  @NotBlank(message = "El texto del comentario no puede estar vacío")
  private String texto;
}
