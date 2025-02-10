package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.DTOs.PerfilDTO;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.example.diadp1backend.Security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class PerfilService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private JWTService jwtService;
  @Autowired
  private PublicacionService publicacionService;

  @Transactional
  public PerfilDTO obtenerPerfilUsuarioLoggeado() {
    // Obtener la solicitud HTTP actual
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractTokenData(token).getUsername();

    System.out.println("🔹 Usuario autenticado: " + username);

    // Buscar usuario por nombre en la base de datos
    Usuario usuario = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    System.out.println("✅ Usuario encontrado: " + usuario.getNombre());





    // Convertir publicaciones a DTO
    List<PublicacionDTO> publicacionesDTO = publicacionService.listarPublicacionesDeUsuario(usuario);

    return new PerfilDTO(
      usuario.getNombre(),
      usuario.getSeguidores().size(),
      usuario.getSeguidos().size(),
      usuario.getCualidad() != null ? usuario.getCualidad().getRaza() : "No disponible",
      usuario.getCualidad() != null ? usuario.getCualidad().getFoto() : null,
      publicacionesDTO
    );
  }
}
