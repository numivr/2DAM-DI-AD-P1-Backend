package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.AuthenticationResponseDTO;
import org.example.diadp1backend.DTOs.LoginDTO;
import org.example.diadp1backend.DTOs.RegistroDTO;
import org.example.diadp1backend.Security.AuthenticationService;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

  @PostMapping("/verificarCuenta")
  public ResponseEntity<String> verificarCuenta(@RequestBody Map<String, String> payload) {
    String usuario = payload.get("usuario");

    if (usuario == null) {
      return ResponseEntity.badRequest().body("❌ Error: Usuario no proporcionado.");
    }

    boolean verificado = usuarioService.verificarUsuario(usuario);
    if (verificado) {
      return ResponseEntity.ok("✅ Mail enviado con éxito.");
    } else {
      return ResponseEntity.badRequest().body("❌ Error: No se pudo enviar el Mail.");
    }
  }

  @PostMapping("/confirmarVerificacion")
  public ResponseEntity<String> confirmarVerificacion(@RequestBody Map<String, String> payload) {
    System.out.println("📥 Request recibida: " + payload);
    String usuario = payload.get("usuario");

    if (usuario == null) {
      return ResponseEntity.badRequest().body("❌ Error: Usuario no proporcionado.");
    }

    boolean verificado = usuarioService.confirmarVerificacionUsuario(usuario);
    if (verificado) {
      return ResponseEntity.ok("✅ Cuenta verificada correctamente.");
    } else {
      return ResponseEntity.badRequest().body("❌ Error: No se pudo verificar la cuenta.");
    }
  }

  /**
   * 🔹 Endpoint para resetear la contraseña de un usuario
   */
  @PostMapping("/resetearContraseña")
  public ResponseEntity<String> resetearContraseña(@RequestBody Map<String, String> payload) {
    if (!payload.containsKey("usuario")) {
      return ResponseEntity.badRequest().body("❌ Error: Usuario no proporcionado.");
    }
    String usuario = payload.get("usuario");

    boolean restablecido = usuarioService.resetearContraseña(usuario);
    if (restablecido) {
      return ResponseEntity.ok("✅ Nueva contraseña enviada por correo.");
    } else {
      return ResponseEntity.badRequest().body("❌ Error al restablecer la contraseña.");
    }
  }



}
