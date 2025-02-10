package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ComentarioDTO;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.Security.JWTService;
import org.example.diadp1backend.Security.TokenDataDTO;
import org.example.diadp1backend.modelos.Comentario;
import org.example.diadp1backend.modelos.Publicacion;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.ComentarioRepository;
import org.example.diadp1backend.repositorios.CualidadRepository;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicacionService {

  private final PublicacionRepository publicacionRepository;
  private final UsuarioRepository usuarioRepository;
  private final JWTService jwtService;
  private final ComentarioRepository comentarioRepository;
  private final CualidadRepository cualidadRepository;

  @Transactional
  public List<PublicacionDTO> listarPublicaciones() {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);
    String username = tokenDataDTO.getUsername();

    System.out.println("Usuario autenticado: " + username);

    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    List<Publicacion> publicaciones = publicacionRepository.findAllWithDetails();


    return publicaciones.stream().map(p -> {
      PublicacionDTO dto = new PublicacionDTO();

      dto.setId(p.getId()); // 👈 Ahora se incluye el ID de la publicación
      dto.setPerfil(p.getCreador().getNombre());
      dto.setFotoPerfil(p.getCreador().getCualidad() != null ? p.getCreador().getCualidad().getFoto() : null);
      dto.setTexto(p.getTexto());
      dto.setFotoPublicacion(p.getImagen());

      // Clonamos las colecciones antes de iterarlas para evitar problemas de concurrencia
      List<Comentario> comentarios = comentarioRepository.obtenerComentariosPorPublicacionId(p.getId());

      Set<Usuario> likes = new HashSet<>(p.getLikes());

      dto.setNumComentarios(comentarios.size());
      dto.setNumLikes(likes.size());
      dto.setLiked(likes.stream().anyMatch(usuario -> usuario.getId().equals(usuarioActual.getId())));

      return dto;
    }).collect(Collectors.toList());
  }

  @Transactional
  public List<PublicacionDTO> listarPublicacionesDeUsuario(Usuario usuario) {
    // Validar si el usuario existe en la base de datos
    Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado con ID: " + usuario.getId()));

    System.out.println("✅ Usuario encontrado: " + usuarioExistente.getNombre());

    // Obtener publicaciones del usuario
    List<Publicacion> publicaciones = new ArrayList<>(usuarioExistente.getPublicaciones());

    if (publicaciones.isEmpty()) {
      System.out.println("⚠️ El usuario no tiene publicaciones.");
    } else {
      System.out.println("📝 Publicaciones encontradas: " + publicaciones.size());
    }

    return publicaciones.stream().map(p -> {
      PublicacionDTO dto = new PublicacionDTO();

      dto.setId(p.getId());
      dto.setPerfil(p.getCreador().getNombre());
      dto.setFotoPerfil(p.getCreador().getCualidad() != null ? p.getCreador().getCualidad().getFoto() : null);
      dto.setTexto(p.getTexto());
      dto.setFotoPublicacion(p.getImagen());

      List<Comentario> comentarios = comentarioRepository.obtenerComentariosPorPublicacionId(p.getId());
      Set<Usuario> likes = new HashSet<>(p.getLikes());

      dto.setNumComentarios((comentarios.size()));
      dto.setNumLikes(likes.size());

      return dto;
    }).collect(Collectors.toList());
  }

  @Transactional
  public PublicacionDTO obtenerPublicacionPorId(Integer id) {
    // Buscar la publicación en el repositorio
    Publicacion publicacion = publicacionRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("❌ Publicación no encontrada con ID: " + id));

    System.out.println("✅ Publicación encontrada con ID: " + id);




    // Convertir a DTO
    PublicacionDTO dto = new PublicacionDTO();
    dto.setId(publicacion.getId());
    dto.setPerfil(publicacion.getCreador().getNombre());
    dto.setFotoPerfil(publicacion.getCreador().getCualidad() != null ? publicacion.getCreador().getCualidad().getFoto() : null);
    dto.setTexto(publicacion.getTexto());
    dto.setFotoPublicacion(publicacion.getImagen());

    // ✅ Formateador para la fecha
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Obtener publicaciones del usuario
    List<Comentario> comentarios = comentarioRepository.obtenerComentariosPorPublicacionId(id);




    // Convertir comentarios a DTO con formato correcto de fecha
    List<ComentarioDTO> comentariosDTO = comentarios
      .stream()
      .map(comentario -> new ComentarioDTO(
        comentario.getId(),
        comentario.getPublicacion().getId(),
        comentario.getUsuario().getNombre(),
        comentario.getUsuario().getCualidad().getFoto(),
        comentario.getTexto(),
        comentario.getFecha().toLocalDateTime().format(formatter) // ✅ Ahora la fecha es un String formateado
      ))
      .collect(Collectors.toList());



    // Manejar likes
    Set<Usuario> likes = publicacion.getLikes();

    dto.setNumComentarios((comentariosDTO.size()));
    dto.setNumLikes(likes.size());
    dto.setComentarios((ArrayList<ComentarioDTO>) comentariosDTO); // 👈 Se agregan los comentarios al DTO

    return dto;
  }






  @Transactional
  public void darLike(Integer idPublicacion, String username) {
    Usuario usuario = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Publicacion publicacion = publicacionRepository.findById(idPublicacion)
      .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

    if (!publicacion.getLikes().contains(usuario)) {
      publicacion.getLikes().add(usuario);
      publicacionRepository.save(publicacion);  // Persistimos el cambio
    }
  }

  @Transactional
  public void quitarLike(Integer idPublicacion, String username) {
    Usuario usuario = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Publicacion publicacion = publicacionRepository.findById(idPublicacion)
      .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

    if (publicacion.getLikes().contains(usuario)) { // Verificamos si ya tiene like
      publicacion.getLikes().remove(usuario); // Eliminamos el usuario del set de likes
      publicacionRepository.save(publicacion); // Persistimos el cambio
    }
  }






}



