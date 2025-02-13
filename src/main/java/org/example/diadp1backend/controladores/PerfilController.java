package org.example.diadp1backend.controladores;


import org.example.diadp1backend.DTOs.PerfilDTO;
import org.example.diadp1backend.servicios.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

  @Autowired
  private PerfilService perfilService;

  @GetMapping("/perfilLoggeado")
  public PerfilDTO obtenerPerfilLoggeado() {
    return perfilService.obtenerPerfilUsuarioLoggeado();
  }

  @GetMapping("/perfilNombre")
    public String obtenerPerfilPorNombre() {
        return perfilService.obtenerNombreUsuarioLoggeado();
    }
}


/*
 */
