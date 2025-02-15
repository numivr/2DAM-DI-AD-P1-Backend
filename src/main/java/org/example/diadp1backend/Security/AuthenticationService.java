package org.example.diadp1backend.Security;

import lombok.RequiredArgsConstructor;
import org.example.diadp1backend.DTOs.AuthenticationResponseDTO;
import org.example.diadp1backend.DTOs.LoginDTO;
import org.example.diadp1backend.DTOs.UsuarioDTO;
import org.example.diadp1backend.converter.UsuarioMapper;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Autowired
  private final UsuarioService usuarioService;
  @Autowired
  private final UsuarioMapper usuarioMapper;
  @Autowired
  private final PasswordEncoder passwordEncoder;
  @Autowired
  private final JWTService jwtService;
  @Autowired
  private final AuthenticationManager authenticationManager;
  @Autowired
  private UsuarioRepository usuarioRepository;


  public AuthenticationResponseDTO register(UsuarioDTO usuarioDTO){
    usuarioDTO.setContraseña(passwordEncoder.encode(usuarioDTO.getContraseña()));
    UsuarioDTO dto = usuarioService.save(usuarioDTO);
    String token = jwtService.generateToken(usuarioMapper.toEntity(dto));
    return AuthenticationResponseDTO
      .builder()
      .token(token)
      .build();
  }

  public AuthenticationResponseDTO login(LoginDTO loginDTO){
    UsuarioDTO user = usuarioService.getByUsername(loginDTO.getUsername());

    Usuario usuario = usuarioRepository.findTopByNombre(loginDTO.getUsername())
      .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado"));

    // 🔹 Verificar si el usuario está baneado
    if (usuario.getBaneado()) {
      return AuthenticationResponseDTO
        .builder()
        .message("⛔ Acceso denegado: Tu cuenta ha sido baneada.")
        .build();
    }

    // 🔹 Verificar si el usuario está verificado
    if (!usuario.getVerificado()) {
      return AuthenticationResponseDTO
        .builder()
        .message("⚠️ No puedes iniciar sesión hasta que tu cuenta sea verificada.")
        .build();
    }

    String usuarioRol = String.valueOf(user.isEsAdmin());
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(),
        loginDTO.getPassword(),
        List.of(new SimpleGrantedAuthority(usuarioRol))
      )
    );
     String token = jwtService.generateToken(usuarioMapper.toEntity(user));
    return  AuthenticationResponseDTO
      .builder()
      .token(token)
      .message("Login success")
      .build();
  }

  public boolean verifyPassword(LoginDTO loginDTO){
    return usuarioService.existByCredentials(loginDTO.getUsername(),loginDTO.getPassword());

  }

}
