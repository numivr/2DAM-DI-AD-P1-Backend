package org.example.diadp1backend.servicios;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.DTOs.UsuarioDTO;
import org.example.diadp1backend.Security.JWTService;
import org.example.diadp1backend.converter.UsuarioMapper;
import org.example.diadp1backend.modelos.Cualidad;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    private final JavaMailSender mailSender; // ✅ Servicio de email

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioMapper usuarioMapper;






  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findTopByNombre(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
  }

  public Usuario registrarUsuario(RegistroDTO dto){

    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setNombre(dto.getUsuario());
    nuevoUsuario.setContraseña(passwordEncoder.encode(dto.getPassword()));
    nuevoUsuario.setEmail(dto.getEmail());
    nuevoUsuario.setEsAdmin(false);

    // 📌 Crear la entidad `Cualidad` y asociarla al usuario
    Cualidad nuevaCualidad = new Cualidad();
    nuevaCualidad.setUsuario(nuevoUsuario);
    nuevaCualidad.setFoto(dto.getFoto());
    nuevaCualidad.setGenero(dto.getGenero().equalsIgnoreCase("Masculino") ? "M" : "F");;
    nuevaCualidad.setEdad(dto.getEdad());
    nuevaCualidad.setRaza(dto.getRaza());
    nuevaCualidad.setNivelEnergia(dto.getNivelEnergia());
    nuevaCualidad.setSociabilidad(dto.getSociabilidad());
    nuevaCualidad.setTamaño(dto.getTamaño());
    nuevaCualidad.setToleranciaEspecies(dto.getToleranciaEspecies());
    nuevaCualidad.setNivelDominancia(dto.getNivelDominancia());
    nuevaCualidad.setTendenciaJuego(dto.getTendenciaJuego());
    nuevaCualidad.setTemperamento(dto.getTemperamento());
    nuevaCualidad.setExperiencia(dto.getExperiencia());
    nuevaCualidad.setTerritorialidad(dto.getTerritorialidad());

    nuevoUsuario.setCualidad(nuevaCualidad);

    return usuarioRepository.save(nuevoUsuario);
  }


  public UsuarioDTO getByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findTopByNombre(username).orElse(null);

    if (usuario!=null){
      return usuarioMapper.toDTO(usuario);
    }else{
      throw  new UsernameNotFoundException("Usuario no encontrado");
    }

  }

  public UsuarioDTO save(UsuarioDTO usuarioDTO){
    return usuarioMapper.toDTO(usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO)));
  }

  public Boolean existByCredentials(String username, String password){
    Usuario usuario = usuarioRepository.findTopByNombre(username).orElse(null);
    return usuario != null  && passwordEncoder.matches(password,usuario.getPassword());
  }


  public boolean credencialDisponible(String nombreUsuario) {

    Usuario usuario = usuarioRepository.findTopByNombre(nombreUsuario).orElse(null);

    if (usuario == null){
      return true;
    }
    return false;
    }

  /**
   * ✅ Seguir a un usuario por su nombre
   */
  @Transactional
  public String seguirUsuario(String nombreUsuario, HttpServletRequest request) {
    String username = obtenerUsuarioAutenticado(request);
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

    Usuario usuarioSeguir = usuarioRepository.findTopByNombre(nombreUsuario)
      .orElseThrow(() -> new RuntimeException("Usuario a seguir no encontrado"));

    if (usuarioActual.getSeguidos().contains(usuarioSeguir)) {
      return "Ya sigues a este usuario";
    }

    usuarioActual.getSeguidos().add(usuarioSeguir);
    usuarioSeguir.getSeguidores().add(usuarioActual);

    usuarioRepository.save(usuarioActual);
    usuarioRepository.save(usuarioSeguir);

    return "Ahora sigues a " + usuarioSeguir.getNombre();
  }

  /**
   * ✅ Dejar de seguir a un usuario por su nombre
   */
  @Transactional
  public String dejarSeguirUsuario(String nombreUsuario, HttpServletRequest request) {
    String username = obtenerUsuarioAutenticado(request);
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

    Usuario usuarioDejarSeguir = usuarioRepository.findTopByNombre(nombreUsuario)
      .orElseThrow(() -> new RuntimeException("Usuario a dejar de seguir no encontrado"));

    if (!usuarioActual.getSeguidos().contains(usuarioDejarSeguir)) {
      return "No sigues a este usuario";
    }

    usuarioActual.getSeguidos().remove(usuarioDejarSeguir);
    usuarioDejarSeguir.getSeguidores().remove(usuarioActual);

    usuarioRepository.save(usuarioActual);
    usuarioRepository.save(usuarioDejarSeguir);

    return "Has dejado de seguir a " + usuarioDejarSeguir.getNombre();
  }


  private String obtenerUsuarioAutenticado(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    return jwtService.extractTokenData(token).getUsername();
  }

  /**
   * ✅ Eliminar usuario por su nombre (solo si es el mismo usuario o es admin)
   */
  @Transactional
  public void eliminarUsuario(String nombreUsuario, HttpServletRequest request) {
    String username = obtenerUsuarioAutenticado(request);
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

    // Buscar el usuario a eliminar en la base de datos por nombre
    Usuario usuarioEliminar = usuarioRepository.findTopByNombre(nombreUsuario)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    // Verificar si el usuario es el mismo o si es un administrador
    if (!usuarioActual.getNombre().equals(usuarioEliminar.getNombre()) && !usuarioActual.getEsAdmin()) {
      throw new RuntimeException("⛔ No tienes permisos para eliminar este usuario");
    }

    // Eliminar las relaciones del usuario antes de eliminarlo
    usuarioEliminar.getSeguidores().forEach(u -> u.getSeguidos().remove(usuarioEliminar));
    usuarioEliminar.getSeguidos().forEach(u -> u.getSeguidores().remove(usuarioEliminar));

    // Eliminar sus publicaciones y comentarios
    usuarioEliminar.getPublicaciones().clear();

    // Eliminar el usuario
    usuarioRepository.delete(usuarioEliminar);
  }



  /**
   * ✅ Método para cambiar el estado de baneo de un usuario.
   */
  @Transactional
  public boolean cambiarEstadoBaneo(String nombreUsuario, HttpServletRequest request) {
    // Obtener el usuario autenticado desde el JWT
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token JWT no presente o mal formado");
    }

    String token = authHeader.substring(7);
    String usernameAutenticado = jwtService.extractTokenData(token).getUsername();

    Usuario usuarioActual = usuarioRepository.findTopByNombre(usernameAutenticado)
            .orElseThrow(() -> new RuntimeException("❌ Usuario autenticado no encontrado"));

    // Verificar si el usuario autenticado es un administrador
    if (!usuarioActual.getEsAdmin()) {
      throw new RuntimeException("⛔ No tienes permisos para banear o desbanear usuarios");
    }

    // Buscar el usuario a banear/desbanear
    Usuario usuario = usuarioRepository.findTopByNombre(nombreUsuario)
            .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    // Cambiar el estado de baneo
    usuario.setBaneado(!usuario.getBaneado());
    usuarioRepository.save(usuario);

    return usuario.getBaneado(); // Retorna el nuevo estado de baneo
  }



  /**
   * 📩 Método para enviar el email de verificación con enlace único
   */
  private void enviarEmailVerificacion(Usuario usuario) {
    String subject = "Activa tu cuenta en Santuario";
    String verificationUrl = "http://localhost:8080/auth/verify?usuario=" + usuario.getNombre();
    String message = "Hola " + usuario.getNombre() + ",\n\nPor favor, haz clic en el siguiente enlace para activar tu cuenta:\n"
      + verificationUrl + "\n\nSi no creaste esta cuenta, ignora este mensaje.";

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(usuario.getEmail());
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }





}
