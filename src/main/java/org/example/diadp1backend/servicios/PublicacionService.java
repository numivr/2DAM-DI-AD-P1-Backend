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
        Usuario usuarioActual = getUsuario();
        List<Publicacion> publicaciones = publicacionRepository.findAllWithDetails();

        return publicaciones.stream().map(p -> {
            PublicacionDTO dto = new PublicacionDTO();
            dto.setId(p.getId());
            dto.setIdCreador(p.getCreador().getId()); // ✅ Ahora incluye el ID del creador
            dto.setPerfil(p.getCreador().getNombre());
            dto.setFotoPerfil(p.getCreador().getCualidad() != null ? p.getCreador().getCualidad().getFoto() : null);
            dto.setTexto(p.getTexto());
            dto.setFotoPublicacion(p.getImagen());

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
            dto.setIdCreador(p.getCreador().getId()); //
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

        Usuario usuarioActual = getUsuario();


        // Convertir a DTO
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setIdCreador(publicacion.getCreador().getId()); //
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
        dto.setLiked(likes.stream().anyMatch(usuario -> usuario.getId().equals(usuarioActual.getId())));

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

    @Transactional
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO, HttpServletRequest request) {
        // Obtener el usuario autenticado desde el token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT no presente o mal formado");
        }

        String token = authHeader.substring(7);
        TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);
        String username = tokenDataDTO.getUsername();

        Usuario usuario = usuarioRepository.findTopByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la nueva publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setTexto(publicacionDTO.getTexto());
        publicacion.setImagen(publicacionDTO.getFotoPublicacion());
        publicacion.setCreador(usuario);

        // Guardar la publicación en la base de datos
        Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

        // Convertir la publicación guardada en DTO y retornarla
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacionGuardada.getId());
        dto.setPerfil(usuario.getNombre());
        dto.setFotoPerfil(usuario.getCualidad() != null ? usuario.getCualidad().getFoto() : null);
        dto.setTexto(publicacionGuardada.getTexto());
        dto.setFotoPublicacion(publicacionGuardada.getImagen());
        dto.setNumComentarios(0);
        dto.setNumLikes(0);
        dto.setLiked(false);

        return dto;
    }


    @Transactional
    public List<PublicacionDTO> listarPublicacionesDeUsuariosSeguidos() {
        Usuario usuarioActual = getUsuario();

        // Obtener el conjunto de usuarios seguidos
        Set<Usuario> usuariosSeguidos = usuarioActual.getSeguidos();

        if (usuariosSeguidos.isEmpty()) {
            System.out.println("⚠️ El usuario no sigue a nadie, devolviendo lista vacía.");
            return new ArrayList<>();
        }

        // Obtener publicaciones de los usuarios seguidos
        List<Publicacion> publicaciones = usuariosSeguidos.stream()
                .flatMap(usuario -> usuario.getPublicaciones().stream())
                .collect(Collectors.toList());

        return getPublicacionDTOS(usuarioActual, publicaciones);
    }


    private List<PublicacionDTO> getPublicacionDTOS(Usuario usuarioActual, List<Publicacion> publicaciones) {
        return publicaciones.stream().map(p -> {
            PublicacionDTO dto = new PublicacionDTO();

            dto.setId(p.getId());
            dto.setIdCreador(p.getCreador().getId()); //
            dto.setPerfil(p.getCreador().getNombre());
            dto.setFotoPerfil(p.getCreador().getCualidad() != null ? p.getCreador().getCualidad().getFoto() : null);
            dto.setTexto(p.getTexto());
            dto.setFotoPublicacion(p.getImagen());

            List<Comentario> comentarios = comentarioRepository.obtenerComentariosPorPublicacionId(p.getId());
            Set<Usuario> likes = new HashSet<>(p.getLikes());

            dto.setNumComentarios(comentarios.size());
            dto.setNumLikes(likes.size());
            dto.setLiked(likes.stream().anyMatch(usuario -> usuario.getId().equals(usuarioActual.getId())));

            return dto;
        }).collect(Collectors.toList());
    }


    private Usuario getUsuario() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT no presente o mal formado");
        }

        String token = authHeader.substring(7);
        TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);
        String username = tokenDataDTO.getUsername();

        System.out.println("Usuario autenticado: " + username);

        // Obtener el usuario autenticado
        Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioActual;
    }


    @Transactional
    public void eliminarPublicacion(Integer id, HttpServletRequest request) {
        // Obtener el usuario autenticado desde el JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT no presente o mal formado");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractTokenData(token).getUsername();

        Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
                .orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

        // Buscar la publicación en la base de datos
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Publicación no encontrada"));

        // Verificar si el usuario es el creador de la publicación o un administrador
        if (!publicacion.getCreador().getId().equals(usuarioActual.getId()) && !usuarioActual.getEsAdmin()) {
            throw new RuntimeException("⛔ No tienes permisos para eliminar esta publicación");
        }

        // Eliminar la publicación y sus comentarios asociados
        comentarioRepository.deleteAll(publicacion.getComentarios());
        publicacionRepository.delete(publicacion);
    }

    @Transactional
    public List<PublicacionDTO> buscarPorTexto(String palabra) {
        List<Publicacion> publicaciones = publicacionRepository.findByTextoContainingIgnoreCase(palabra);

        if (palabra.startsWith("@")) {
            String username = palabra.substring(1);
            publicaciones = publicacionRepository.findByCreadorNombreContainingIgnoreCase(username);
        } else {
            publicaciones = publicacionRepository.findByTextoContainingIgnoreCase(palabra);
        }

        return publicaciones.stream().map(pub -> new PublicacionDTO(
                pub.getId(),
                pub.getCreador().getId(),
                pub.getCreador().getNombre(),
                pub.getCreador().getCualidad() != null ? pub.getCreador().getCualidad().getFoto() : null,
                pub.getTexto(),
                pub.getImagen(),
                pub.getComentarios().size(),
                pub.getLikes().size(),
                pub.getLikes().stream().anyMatch(usuario -> usuario.getId().equals(getUsuario().getId())),
                new ArrayList<>()
        )).collect(Collectors.toList());
    }
}



