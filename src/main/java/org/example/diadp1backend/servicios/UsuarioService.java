package org.example.diadp1backend.servicios;


import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.DTOs.UsuarioDTO;
import org.example.diadp1backend.converter.UsuarioMapper;
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



}
