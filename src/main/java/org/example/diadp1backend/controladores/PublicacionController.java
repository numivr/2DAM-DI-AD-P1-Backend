package org.example.diadp1backend.controladores;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.example.diadp1backend.servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

  private PublicacionService publicacionService;

  @GetMapping("/listarPublicaciones")
  public List<PublicacionDTO> listarPublicaciones() {
    return publicacionService.listarPublicaciones();
  }

  @GetMapping("/{id}")
  public PublicacionDTO obtenerPublicacionPorId(@PathVariable Integer id) {
    return publicacionService.obtenerPublicacionPorId(id);
  }

  @PostMapping("/{idPublicacion}")
  public ResponseEntity<?> darLike(@PathVariable Integer idPublicacion, Principal principal) {
    String username = principal.getName();
    publicacionService.darLike(idPublicacion, username);
    return ResponseEntity.ok("Like agregado");
  }

  @DeleteMapping("/{idPublicacion}")
  public ResponseEntity<?> quitarLike(@PathVariable Integer idPublicacion, Principal principal) {
    String username = principal.getName();
    publicacionService.quitarLike(idPublicacion, username);
    return ResponseEntity.ok("Like eliminado");
  }

  @PostMapping("/crear")
  public ResponseEntity<?> crearPublicacion(@RequestBody PublicacionDTO publicacionDTO, HttpServletRequest request) {
    try {
      PublicacionDTO nuevaPublicacion = publicacionService.crearPublicacion(publicacionDTO, request);
      return ResponseEntity.ok(nuevaPublicacion);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("❌ Error al crear la publicación: " + e.getMessage());
    }
  }

  @GetMapping("/siguiendo")
  public List<PublicacionDTO> listarPublicacionesDeUsuariosSeguidos() {
    return publicacionService.listarPublicacionesDeUsuariosSeguidos();
  }


  @DeleteMapping("/eliminar/{id}")
  public ResponseEntity<String> eliminarPublicacion(@PathVariable Integer id, HttpServletRequest request) {
    try {
      publicacionService.eliminarPublicacion(id, request);
      return ResponseEntity.ok("✅ Publicación eliminada correctamente");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("❌ Error al eliminar publicación: " + e.getMessage());
    }
  }

  @GetMapping("/buscar")
  public List<PublicacionDTO> buscarPublicaciones(@RequestParam String palabra) {
    return publicacionService.buscarPorTexto(palabra);
  }


}
