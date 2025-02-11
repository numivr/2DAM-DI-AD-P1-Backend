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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

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

  @Transactional
  public String seguirUsuario(Integer id, HttpServletRequest request) {

    String username = obtenerUsuarioAutenticado(request);
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

    Usuario usuarioSeguir = usuarioRepository.findById(id)
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



  @Transactional
  public String dejarSeguirUsuario(Integer id, HttpServletRequest request) {
    String username = obtenerUsuarioAutenticado(request);
    Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
      .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

    Usuario usuarioDejarSeguir = usuarioRepository.findById(id)
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




}
