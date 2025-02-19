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
import java.util.Optional;

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



    boolean siguiendo = false;

    // Convertir publicaciones a DTO
    List<PublicacionDTO> publicacionesDTO = publicacionService.listarPublicacionesDeUsuario(usuario);

    return new PerfilDTO(
      usuario.getNombre(),
      usuario.getSeguidores().size(),
      usuario.getSeguidos().size(),
      usuario.getCualidad() != null ? usuario.getCualidad().getRaza() : "No disponible",
      usuario.getCualidad() != null ? usuario.getCualidad().getFoto() : null,
      publicacionesDTO,
      siguiendo,
      usuario.getEsAdmin(),
      usuario.getBaneado()
    );
  }


    @Transactional
    public PerfilDTO obtenerPerfilPorId(Integer id) {

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
      Optional<Usuario> usuarioActualOptional = usuarioRepository.findTopByNombre(username);
      Usuario usuarioActual = usuarioActualOptional.orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

    Usuario usuario = usuarioRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado con ID: " + id));


      // Verificar si el usuario loggeado sigue a este perfil
      boolean siguiendo = usuario.getSeguidores().contains(usuarioActual);

    List<PublicacionDTO> publicacionesDTO = publicacionService.listarPublicacionesDeUsuario(usuario);

      return new PerfilDTO(
        usuario.getNombre(),
        usuario.getSeguidores().size(),
        usuario.getSeguidos().size(),
        usuario.getCualidad() != null ? usuario.getCualidad().getRaza() : "No disponible",
        usuario.getCualidad() != null ? usuario.getCualidad().getFoto() : null,
        publicacionesDTO,
        siguiendo,
        usuario.getEsAdmin(),
        usuario.getBaneado()
      );
    }


  @Transactional
  public PerfilDTO obtenerPerfilPorNombre(String nombrePerfil) {
    // Obtener la solicitud HTTP actual
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractTokenData(token).getUsername();

    System.out.println("🔹 Usuario autenticado: " + username);

    // Buscar el usuario autenticado
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

    // Buscar el usuario cuyo perfil se quiere consultar
    Usuario usuarioPerfil = usuarioRepository.findTopByNombre(nombrePerfil)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado con nombre: " + nombrePerfil));

    // Verificar si el usuario loggeado sigue a este perfil
    boolean siguiendo = usuarioPerfil.getSeguidores().contains(usuarioActual);

    // Obtener sus publicaciones
    List<PublicacionDTO> publicacionesDTO = publicacionService.listarPublicacionesDeUsuario(usuarioPerfil);

    return new PerfilDTO(
      usuarioPerfil.getNombre(),
      usuarioPerfil.getSeguidores().size(),
      usuarioPerfil.getSeguidos().size(),
      usuarioPerfil.getCualidad() != null ? usuarioPerfil.getCualidad().getRaza() : "No disponible",
      usuarioPerfil.getCualidad() != null ? usuarioPerfil.getCualidad().getFoto() : null,
      publicacionesDTO,
      siguiendo,
      usuarioPerfil.getEsAdmin(),
      usuarioPerfil.getBaneado()
    );
  }

  public String obtenerNombreUsuarioLoggeado() {
    // Obtener la solicitud HTTP actual
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    return jwtService.extractTokenData(token).getUsername();
  }
}
