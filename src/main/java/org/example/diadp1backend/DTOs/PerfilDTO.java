package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDTO {
  private String nombre;
  private int numeroSeguidores;
  private int numeroSeguidos;
  private String raza;
  private String fotoPerfil;
  private List<PublicacionDTO> publicaciones;
  private boolean siguiendo;  // ✅ Se agrega el atributo siguiendo
  private boolean esAdmin;
}
