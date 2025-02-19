package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.DTOs.ComentarioRequestDTO;
import org.example.diadp1backend.DTOs.ComentarioDTO;
import org.example.diadp1backend.modelos.Comentario;
import org.example.diadp1backend.modelos.Publicacion;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.ComentarioRepository;
import org.example.diadp1backend.repositorios.CualidadRepository;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.example.diadp1backend.Security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ComentarioService {

  @Autowired
  private ComentarioRepository comentarioRepository;

  @Autowired
  private PublicacionRepository publicacionRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private JWTService jwtService;

  @Autowired
  private CualidadRepository cualidadRepository;

  @Transactional
  public ComentarioDTO crearComentario(ComentarioRequestDTO comentarioRequest, HttpServletRequest request) {
    // Obtener el usuario desde el JWT
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractTokenData(token).getUsername();

    Usuario usuario = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    // Buscar la publicación en la base de datos
    Publicacion publicacion = publicacionRepository.findById(comentarioRequest.getIdPublicacion())
      .orElseThrow(() -> new RuntimeException("❌ Publicación no encontrada"));

    // Crear nuevo comentario
    Comentario comentario = new Comentario();
    comentario.setPublicacion(publicacion);
    comentario.setUsuario(usuario);
    comentario.setTexto(comentarioRequest.getTexto());
    comentario.setFecha(Timestamp.from(Instant.now())); // Fecha actual

    // Guardar en la base de datos
    comentarioRepository.save(comentario);


    // Devolver el comentario en formato DTO
    return new ComentarioDTO(
      comentario.getId(),
      comentario.getPublicacion().getId(),
      comentario.getUsuario().getNombre(),
      comentario.getUsuario().getCualidad().getFoto(),
      comentario.getTexto(),
      comentario.getFecha().toString()
    );
  }

  @Transactional
  public void eliminarComentario(Integer id, HttpServletRequest request) {
    // Obtener el usuario autenticado desde el JWT
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractTokenData(token).getUsername();

    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

    // Buscar el comentario en la base de datos
    Comentario comentario = comentarioRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("❌ Comentario no encontrado"));

    // Verificar si el usuario es el creador del comentario o un administrador
    if (!comentario.getUsuario().getId().equals(usuarioActual.getId()) && !usuarioActual.getEsAdmin()) {
      throw new RuntimeException("⛔ No tienes permisos para eliminar este comentario");
    }

    // Eliminar el comentario
    comentarioRepository.delete(comentario);
  }



}
