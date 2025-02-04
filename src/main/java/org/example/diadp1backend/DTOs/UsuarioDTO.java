package org.example.diadp1backend.DTOs;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;


@Data
public class UsuarioDTO {
  private Integer id; //no se si es necesario
  private String nombre;
  private String email;
  private String contraseña; //hay que ver como se hace la creación de usuario
  private boolean esAdmin;





}
