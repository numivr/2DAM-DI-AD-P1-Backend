package org.example.diadp1backend.controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.diadp1backend.DTOs.ComentarioRequestDTO;
import org.example.diadp1backend.DTOs.ComentarioDTO;
import org.example.diadp1backend.servicios.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {

  @Autowired
  private ComentarioService comentarioService;

  @PostMapping("/crear")
  public ResponseEntity<ComentarioDTO> crearComentario(
    @Valid @RequestBody ComentarioRequestDTO comentarioRequest,
    HttpServletRequest request) {
    ComentarioDTO comentarioDTO = comentarioService.crearComentario(comentarioRequest, request);
    return ResponseEntity.ok(comentarioDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> eliminarComentario(@PathVariable Integer id, HttpServletRequest request) {
    try {
      comentarioService.eliminarComentario(id, request);
      return ResponseEntity.ok("✅ Comentario eliminado correctamente");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("❌ Error al eliminar comentario: " + e.getMessage());
    }
  }


}
