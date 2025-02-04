package org.example.diadp1backend.converter;



import org.example.diadp1backend.DTOs.PublicacionDTO;
import org.example.diadp1backend.modelos.Publicacion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublicacionMapper {

  PublicacionDTO toDTO(Publicacion entity);

  Publicacion toEntity(PublicacionDTO dto);

  List<PublicacionDTO> toDTO(List<Publicacion> listEntity);

  List<Publicacion> toEntity(List<PublicacionDTO> listDTOs);

}
