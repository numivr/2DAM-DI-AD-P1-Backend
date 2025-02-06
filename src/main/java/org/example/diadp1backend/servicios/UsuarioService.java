package org.example.diadp1backend.servicios;


import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.UsuarioRepository;
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


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findTopByNombre(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
  }

  public Usuario registrarUsuario(RegistroDTO dto){

    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setNombre(dto.getUsuario());
    nuevoUsuario.setContraseña(passwordEncoder.encode(dto.getPassword()));
    nuevoUsuario.setEsAdmin(false);

    return usuarioRepository.save(nuevoUsuario);
  }









}
