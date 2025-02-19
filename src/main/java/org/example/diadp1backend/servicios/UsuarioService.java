package org.example.diadp1backend.servicios;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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



  public boolean enviarEmailVerificacion(Usuario usuario) {
    try {
      String subject = "Activa tu cuenta en Santuario";
      String verificationUrl = "https://twodam-di-ad-p1-backend-2.onrender.com/auth/verificarCuenta?usuario=" + usuario.getNombre();
      String message = "Hola " + usuario.getNombre() + ",\n\nPor favor, haz clic en el siguiente enlace para activar tu cuenta:\n"
        + verificationUrl + "\n\nSi no creaste esta cuenta, ignora este mensaje.";

      SimpleMailMessage email = new SimpleMailMessage();
      email.setTo(usuario.getEmail());
      email.setSubject(subject);
      email.setText(message);
      mailSender.send(email);

      return true; // ✅ Retorna true si el email se envía correctamente
    } catch (Exception e) {
      e.printStackTrace();
      return false; // ❌ Retorna false si ocurre un error al enviar el email
    }
  }

  public boolean verificarUsuario(String usuario) {
    Usuario usuarioVerificar = usuarioRepository.findTopByNombre(usuario)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    try {
      enviarEmailVerificacion(usuarioVerificar);
      return true; // ✅ Retorna true si el email se envía correctamente
    } catch (Exception e) {
      e.printStackTrace();
      return false; // ❌ Retorna false si ocurre un error al enviar el email
    }
  }


  @Transactional
  public boolean confirmarVerificacionUsuario(String usuario) {
    Usuario usuarioVerificar = usuarioRepository.findTopByNombre(usuario)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    if (!usuarioVerificar.getVerificado()) {
      usuarioVerificar.setVerificado(true);
      usuarioRepository.save(usuarioVerificar);
      return true; // ✅ Éxito
    }
    return false; // ❌ Ya estaba verificado
  }

  /**
   * 🔹 Método para restablecer la contraseña de un usuario.
   */
  @Transactional
  public boolean resetearContraseña(String nombreUsuario) {
    Usuario usuario = usuarioRepository.findTopByNombre(nombreUsuario)
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    // 🔑 Generar una contraseña aleatoria de 10 dígitos numéricos
    String nuevaContraseña = RandomStringUtils.randomNumeric(10);

    // 🔐 Codificar la nueva contraseña antes de guardarla
    usuario.setContraseña(passwordEncoder.encode(nuevaContraseña));

    usuarioRepository.save(usuario); // Guardar los cambios

    // 📩 Enviar la nueva contraseña por correo
    return enviarEmailNuevaContraseña(usuario, nuevaContraseña);
  }

  /**
   * 📩 Método para enviar la nueva contraseña al correo del usuario.
   */
  private boolean enviarEmailNuevaContraseña(Usuario usuario, String nuevaContraseña) {
    try {
      String subject = "🔑 Restablecimiento de Contraseña - Santuario";
      String message = "Hola " + usuario.getNombre() + ",\n\n" +
        "Tu contraseña ha sido restablecida. Aquí está tu nueva contraseña temporal:\n\n" +
        "🔑 " + nuevaContraseña + "\n\n" +
        "Por favor, inicia sesión con esta contraseña y cámbiala lo antes posible.\n\n" +
        "Si no solicitaste este cambio, ignora este mensaje.";

      SimpleMailMessage email = new SimpleMailMessage();
      email.setTo(usuario.getEmail());
      email.setSubject(subject);
      email.setText(message);
      mailSender.send(email);

      return true; // ✅ Enviado correctamente
    } catch (Exception e) {
      e.printStackTrace();
      return false; // ❌ Error al enviar el email
    }
  }



}
