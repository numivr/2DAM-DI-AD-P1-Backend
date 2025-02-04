package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.example.diadp1backend.servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

  private PublicacionService publicacionService;

  @GetMapping("/listarPublicaciones")
  public List<PublicacionDTO> listarPublicaciones() {
    return publicacionService.listarPublicaciones();
  }





}
