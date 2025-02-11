package org.example.diadp1backend.controladores;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("/{id}/seguir")
  public String seguirUsuario(@PathVariable Integer id, HttpServletRequest request) {
    return usuarioService.seguirUsuario(id, request);
  }

  @DeleteMapping("/{id}/dejarSeguir")
  public String dejarSeguirUsuario(@PathVariable Integer id, HttpServletRequest request) {
    return usuarioService.dejarSeguirUsuario(id, request);
  }
}
