package org.example.diadp1backend.controladores;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("/{nombreUsuario}/seguir")
  public String seguirUsuario(@PathVariable String nombreUsuario, HttpServletRequest request) {
    return usuarioService.seguirUsuario(nombreUsuario, request);
  }

  @DeleteMapping("/{nombreUsuario}/dejarSeguir")
  public String dejarSeguirUsuario(@PathVariable String nombreUsuario, HttpServletRequest request) {
    return usuarioService.dejarSeguirUsuario(nombreUsuario, request);
  }

  @DeleteMapping("/eliminar/{nombreUsuario}")
  public ResponseEntity<String> eliminarUsuario(@PathVariable String nombreUsuario, HttpServletRequest request) {
    try {
      usuarioService.eliminarUsuario(nombreUsuario, request);
      return ResponseEntity.ok("✅ Usuario eliminado correctamente");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("❌ Error al eliminar usuario: " + e.getMessage());
    }
  }

  /**
   * ✅ Endpoint para cambiar el estado de baneo de un usuario.
   */
  @PutMapping("/banear/{nombreUsuario}")
  public ResponseEntity<String> cambiarEstadoBaneo(@PathVariable String nombreUsuario, HttpServletRequest request) {
    try {
      boolean nuevoEstado = usuarioService.cambiarEstadoBaneo(nombreUsuario, request);
      String mensaje = nuevoEstado ? "🚫 Usuario baneado correctamente" : "✅ Usuario desbaneado correctamente";
      return ResponseEntity.ok(mensaje);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("❌ Error al cambiar estado de baneo: " + e.getMessage());
    }
  }




}
