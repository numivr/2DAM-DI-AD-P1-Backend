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

  @PostMapping("/{id}/seguir")
  public String seguirUsuario(@PathVariable Integer id, HttpServletRequest request) {
    return usuarioService.seguirUsuario(id, request);
  }

  @DeleteMapping("/{id}/dejarSeguir")
  public String dejarSeguirUsuario(@PathVariable Integer id, HttpServletRequest request) {
    return usuarioService.dejarSeguirUsuario(id, request);
  }

  @DeleteMapping("/eliminar/{id}")
  public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id, HttpServletRequest request) {
    try {
      usuarioService.eliminarUsuario(id, request);
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
