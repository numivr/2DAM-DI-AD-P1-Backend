package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.AuthenticationResponseDTO;
import org.example.diadp1backend.DTOs.LoginDTO;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.Security.AuthenticationService;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private UsuarioService usuarioService;

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/registro")
  public Usuario registro(@RequestBody RegistroDTO registroDTO){

    return usuarioService.registrarUsuario(registroDTO);
  }

  @PostMapping("/login")
  public AuthenticationResponseDTO register(@RequestBody LoginDTO loginDTO){
    if(authenticationService.verifyPassword(loginDTO)){
      return authenticationService.login(loginDTO);
    }else{
      return AuthenticationResponseDTO.builder().message("Invalid credentials").build();
    }
  }

  @GetMapping("/credencialDisponible")
  public boolean credencialDisponible(@RequestParam String nombreUsuario){
    return usuarioService.credencialDisponible(nombreUsuario);
  }

}
