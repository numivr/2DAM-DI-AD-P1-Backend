package org.example.diadp1backend.servicios;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.converter.PublicacionMapper;
import org.example.diadp1backend.modelos.Publicacion;
import org.example.diadp1backend.repositorios.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PublicacionService {


  private final PublicacionRepository publicacionRepository;

  @Autowired
  private PublicacionMapper publicacionMapper;


  public List<PublicacionDTO> listarPublicaciones() {


      //Este metodo es algo provisional, la busqueda de publicaciones deberia ser mas especifica
      List<Publicacion> publicaciones = publicacionRepository.findAll();
      List<PublicacionDTO> publicacionesDTO = new ArrayList<>();

      publicacionesDTO = publicacionMapper.toDTO(publicaciones);

      return publicacionesDTO;
    }
}
