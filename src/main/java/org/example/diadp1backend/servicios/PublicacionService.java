package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.Security.JWTService;
import org.example.diadp1backend.Security.TokenDataDTO;
import org.example.diadp1backend.converter.PublicacionMapper;
import org.example.diadp1backend.modelos.Comentario;
import org.example.diadp1backend.modelos.Publicacion;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

  @Autowired
  private PublicacionMapper publicacionMapper;


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

      dto.setPerfil(p.getCreador().getNombre());
      dto.setFotoPerfil(p.getCreador().getCualidad() != null ? p.getCreador().getCualidad().getFoto() : null);
      dto.setTexto(p.getTexto());
      dto.setFotoPublicacion(p.getImagen());

      // Clone collections before iterating
      List<Comentario> comentarios = new ArrayList<>(p.getComentarios());
      Set<Usuario> likes = new HashSet<>(p.getLikes());

      dto.setNumComentarios(String.valueOf(comentarios.size()));
      dto.setNumLikes(likes.size());
      dto.setLiked(likes.stream().anyMatch(usuario -> usuario.getId().equals(usuarioActual.getId())));

      return dto;
    }).collect(Collectors.toList());
  }
}
