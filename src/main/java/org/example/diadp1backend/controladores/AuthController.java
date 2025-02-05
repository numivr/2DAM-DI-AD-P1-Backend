package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private UsuarioService usuarioService;

  @PostMapping("/registro")
  public Usuario registro(RegistroDTO registroDTO){

    return usuarioService.registrarUsuario(registroDTO);
  }
}
